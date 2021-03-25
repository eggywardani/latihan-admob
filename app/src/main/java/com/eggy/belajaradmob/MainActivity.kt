package com.eggy.belajaradmob

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eggy.belajaradmob.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val TAG = "tag"
    private lateinit var binding: ActivityMainBinding

    private var interstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)
        initBannerAds()
        initInterAd()
        initRewardedAd()
        initAdsAction()
        initAction()
    }

    private fun initRewardedAd() {
        val idUnit = "ca-app-pub-3940256099942544/5224354917"
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, idUnit, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mRewardedAd = null
            }

        }


    }

    private fun initAction() {
        binding.btnInterAdd.setOnClickListener {
            showInterAd()
        }

        binding.btnRewarded.setOnClickListener {
            showRewarded()
        }
    }

    private fun initAdsAction() {
        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Snackbar.make(binding.root, "Iklan tampil", Snackbar.LENGTH_SHORT).show()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                Snackbar.make(
                    binding.root,
                    "Iklan gagal tampil karena ${p0?.message}",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
        }

    }

    private fun initInterAd() {
        val idAd = "ca-app-pub-3940256099942544/1033173712"
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, idAd, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.e(TAG, p0.message)
                interstitialAd = null
            }

            override fun onAdLoaded(p0: InterstitialAd) {
                interstitialAd = p0
            }
        })

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                interstitialAd = null;
            }
        }


    }

    private fun showInterAd() {
        if (interstitialAd != null) {
            interstitialAd?.show(this)
        } else {
            Snackbar.make(binding.root, "Iklan gagal tampil", Snackbar.LENGTH_SHORT)
        }
    }

    private fun showRewarded() {
        if (mRewardedAd != null) {
            mRewardedAd?.show(this, OnUserEarnedRewardListener {
                fun onUserEarnedReward(rewardItem: RewardItem) {

                    Log.d("TAG", "User earned the ${it.amount} and ${it.type} ")
                }
            })
        } else {
            Snackbar.make(binding.root, "Iklan gagal tampil", Snackbar.LENGTH_SHORT)
        }
    }


    private fun initBannerAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


    }
}