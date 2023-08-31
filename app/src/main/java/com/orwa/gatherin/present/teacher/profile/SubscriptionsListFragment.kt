package com.orwa.gatherin.present.teacher.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.orwa.gatherin.R
import com.orwa.gatherin.base.AuthNetworkState
import com.orwa.gatherin.databinding.FragmentSubscriptionsListBinding
import com.orwa.gatherin.base.BaseTeacherFragment
import com.orwa.gatherin.model.pack.SaveReceiptReq
import com.orwa.gatherin.model.profile.PacksRes
import com.orwa.gatherin.model.profile.PacksResItem
import com.orwa.gatherin.present.start.StartActivity
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import kotlinx.coroutines.launch


class SubscriptionsListFragment : BaseTeacherFragment(), PurchasesUpdatedListener {

    private val TAG = SubscriptionsListFragment::class.java.simpleName

    lateinit var binding: FragmentSubscriptionsListBinding

    private val viewModel: SubscriptionsViewModel by viewModels()

    val request = { viewModel.getSubscriptionsList() }

//    val requestBuyPack = { selectedPack?.let { viewModel.buyPackage(it.id) } }

    val requestSaveReceipt = { saveReceiptParam: SaveReceiptReq ->
        viewModel.saveReceipt(saveReceiptParam)
    }


    private lateinit var subscriptionsAdapter: SubscriptionsRecyclerViewAdapter

    private lateinit var billingClient: BillingClient

    private var silverPackMonthly: SkuDetails? = null
    private var silverPackAnnual: SkuDetails? = null
    private var goldenPackMonthly: SkuDetails? = null
    private var goldenPackAnnual: SkuDetails? = null

    private var selectedPack: PacksResItem? = null
    private var lastPurchases: List<Purchase?>? = null

    private val packs = ArrayList<SkuDetails>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBilling()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSubscriptionsListBinding.inflate(inflater, container, false)

        setupRV()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaderTitle(getString(R.string.subscriptionsList))

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                binding.rvSubscriptionsList.showShimmerAdapter()
                binding.rvEmpty.hide()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> toastFailure(R.string.connection_error)
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                binding.rvSubscriptionsList.hideShimmerAdapter()

            }
        })

        viewModel.subscriptionsListRes.observe(viewLifecycleOwner, Observer {
//            Log.i(TAG, "departments=$it")
            if (it != null) {
//                changeCanBuyForPackages(it)
                subscriptionsAdapter.swapData(it)
            } else {
                request.invoke()
            }
        })

        viewModel.buyPackNetworkState.observe(viewLifecycleOwner, Observer {
            if (it == AuthNetworkState.LOADING) {
                showProgressDialog()
            } else {
                when (it) {
                    AuthNetworkState.SUCCESS -> {
                    }
                    AuthNetworkState.USER_UNAUTHORIZED -> toastFailure(R.string.user_unauthorized)
                    AuthNetworkState.CONNECT_ERROR -> showRetryConnectionSnackBar {
//                        requestBuyPack.invoke()
                        getSaveReceiptParam()?.let { requestSaveReceipt.invoke(it) }

                    }
                    AuthNetworkState.FAILURE -> toastFailure(R.string.network_error)
                    else -> {
                    }
                }
                hideProgressDialog()

            }
        })

//        viewModel.payPackRes.observe(viewLifecycleOwner, Observer {
//
//        })

        viewModel.saveReceiptRes.observe(viewLifecycleOwner, Observer {
            it?.let {
                continueThePurchase()
            }
        })
    }

    fun changeCanBuyForPackages(packs: List<PacksResItem>) {
        val currentPack = activityViewModel.userInfo.value?.pacakgeInfo
        if (currentPack == null) { //No previous subscription or it was cancelled
            for (pack in packs) {
                pack.isSubscribed = false
                pack.canBuy = true
            }
        } else {
            for (pack in packs) {
                pack.canBuy = false
                if (pack.packID == currentPack.packAndroidID) {
                    pack.isSubscribed = true
                } else {
                    pack.isSubscribed = false
                }
            }
        }
    }

    private fun setupRV() {
        // Set the adapter
        val view = binding.rvSubscriptionsList
        with(view) {
            layoutManager = LinearLayoutManager(context)
            subscriptionsAdapter = SubscriptionsRecyclerViewAdapter(
                listOf(),
                itemCallBack,
                requireContext(),
                binding.rvEmpty,
                activityViewModel.userInfo.value?.pacakgeInfo
            )
            adapter = subscriptionsAdapter
        }
    }

    private val itemCallBack = { item: PacksResItem ->
        selectedPack = item
        when (item.packID) {
            FREE_PACK -> toastSmall(R.string.free_pack_click)
            SILVER_PACK_MONTHLY1 -> silverPackMonthly?.let { launchPurchaseFlow(it) }
                ?: toastSmall(R.string.error_getting_pack)
            SILVER_PACK_ANNUAL1 -> silverPackAnnual?.let { launchPurchaseFlow(it) }
                ?: toastSmall(R.string.error_getting_pack)
            GOLDEN_PACK_MONTHLY1 -> goldenPackMonthly?.let { launchPurchaseFlow(it) }
                ?: toastSmall(R.string.error_getting_pack)
            GOLDEN_PACK_ANNUAL1 -> goldenPackAnnual?.let { launchPurchaseFlow(it) }
                ?: toastSmall(R.string.error_getting_pack)
        }

    }


    fun initBilling() {
        // Set up the billing client
        billingClient = BillingClient
            .newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                    Log.i(TAG, "billing_Billing client successfully set up")
                    queryOneTimeProducts()
                } else {
                    toastFailure(R.string.error_connecting_to_billing)
                }
            }

            override fun onBillingServiceDisconnected() {
                toastFailure(R.string.billing_service_disconnected)
//                Log.i(TAG, "billing_Billing service disconnected")
            }
        })
    }

    private fun queryOneTimeProducts() {
        val skuListToQuery = ArrayList<String>()

        skuListToQuery.add(SILVER_PACK_MONTHLY1)
        skuListToQuery.add(SILVER_PACK_ANNUAL1)
        skuListToQuery.add(GOLDEN_PACK_MONTHLY1)
        skuListToQuery.add(GOLDEN_PACK_ANNUAL1)
        // ‘coins_5’ is the product ID that was set in the Play Console.
        // Here is where we can add more product IDs to query for based on
        //   what was set up in the Play Console.

        val params = SkuDetailsParams.newBuilder()
        params
            .setSkusList(skuListToQuery)
            .setType(BillingClient.SkuType.SUBS)
        // SkuType.INAPP refers to 'managed products' or one time purchases.
        // To query for subscription products, you would use SkuType.SUBS.

        billingClient.querySkuDetailsAsync(
            params.build(),
            object : SkuDetailsResponseListener {

                override fun onSkuDetailsResponse(p0: BillingResult, p1: MutableList<SkuDetails>?) {
//                    Log.i(TAG, "billing_onSkuDetailsResponse ${p0.responseCode}")
                    if (p1 != null) {
                        for (skuDetail in p1) {
                            Log.i(TAG, skuDetail.toString())

                            if (skuDetail.sku.equals(SILVER_PACK_MONTHLY1, ignoreCase = true)) {
                                silverPackMonthly = skuDetail
                                continue
                            } else if (skuDetail.sku.equals(
                                    SILVER_PACK_ANNUAL1,
                                    ignoreCase = true
                                )
                            ) {
                                silverPackAnnual = skuDetail
                                continue
                            } else if (skuDetail.sku.equals(
                                    GOLDEN_PACK_MONTHLY1,
                                    ignoreCase = true
                                )
                            ) {
                                goldenPackMonthly = skuDetail
                                continue
                            } else if (skuDetail.sku.equals(
                                    GOLDEN_PACK_ANNUAL1,
                                    ignoreCase = true
                                )
                            ) {
                                goldenPackAnnual = skuDetail
                                continue
                            }

                        }
                    } else {
//                        Log.i(TAG, "billing_No skus found from query")
                    }
                }
            })
    }

    // Google Play calls this method to propagate the result of the purchase flow
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase?>?) {
        if (billingResult.responseCode == OK && purchases != null) {
            lastPurchases = purchases
            //set the package was bought
//            Log.i(TAG, "billing_PURCHASE=${purchases.get(0)}")
            getSaveReceiptParam()?.let { requestSaveReceipt.invoke(it) }

        } else if (billingResult.responseCode == USER_CANCELED) {
            toastSmall(R.string.process_cancelled)
//            Log.i(TAG, "billing_User cancelled purchase flow.")
        } else {
            toastSmall(R.string.network_error)
//            Log.i(TAG, "billing_onPurchaseUpdated error: ${billingResult?.responseCode}")
        }
    }

    fun continueThePurchase() {
        lastPurchases?.let {
            for (purchase in lastPurchases!!) {
                lifecycleScope.launch {
                    purchase?.let { handlePurchase(it) }
                }
            }
        }

    }

    private suspend fun handlePurchase(purchase: Purchase) {
        // If your app has a server component, first verify the purchase by checking that the
        // purchaseToken hasn't already been used.

        // If purchase was a consumable product (a product you want the user to be able to buy again)
//        handleConsumableProduct(purchase)

        // If purchase was non-consumable product
        handleNonConsumableProduct(purchase)
    }

//    fun handleConsumableProduct(purchase: Purchase) {
//        val consumeParams =
//            ConsumeParams.newBuilder()
//                .setPurchaseToken(purchase.getPurchaseToken())
//                .build()
//
//        billingClient.consumeAsync(consumeParams, { billingResult, purchaseToken ->
//            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                // Handle the success of the consume operation.
//            }
//        })
//    }

    suspend fun handleNonConsumableProduct(purchase: Purchase) {
        if (purchase.purchaseState == PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult =
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
//                Log.i(TAG, "billing_ACK_RESPONSE=$ackPurchaseResult")
                if (ackPurchaseResult.responseCode == OK) {
//                    Log.i(TAG, "billing_ACK_RESPONSE_OK=OK")

                    Util.show1OptionMsgDialog(requireContext(), R.string.buy_package_success) {
                        startActivityWithClear(StartActivity::class.java)
                        requireActivity().finish()
                    }

                }
            }
        }
    }


//    /**
//     * Query Google Play Billing for existing purchases.
//     *
//     * New purchases will be provided to PurchasesUpdatedListener.
//     */
//    fun queryPurchases() {
//        if (!billingClient.isReady) {
//            Log.e(TAG, "queryPurchases: BillingClient is not ready")
//        }
//        // Query for existing in app products that have been purchased. This does NOT include subscriptions.
//        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
//        if (result.purchasesList == null) {
//            Log.i(TAG, "No existing in app purchases found.")
//        } else {
//            Log.i(TAG, "Existing purchases: ${result.purchasesList}")
//        }
//    }


    fun launchPurchaseFlow(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(requireActivity(), flowParams)
//        Log.i(TAG, "billing_launchPurchaseFlow result ${responseCode}")
    }

    fun getSaveReceiptParam(): SaveReceiptReq? {
        if (selectedPack != null && lastPurchases != null) {
            val purchase = com.orwa.gatherin.model.pack.Purchase(
                selectedPack!!.packID,
                lastPurchases!![0]?.purchaseToken.toString()
            )
            val req = SaveReceiptReq(APP_TYPE, purchase)
            return req
        }
        return null
    }

    companion object {
        const val SILVER_PACK_MONTHLY1 = "silver_pack_monthly1"
        const val SILVER_PACK_ANNUAL1 = "silver_pack_annual1"
        const val GOLDEN_PACK_MONTHLY1 = "golden_pack_monthly1"
        const val GOLDEN_PACK_ANNUAL1 = "golden_pack_annual1"

        const val FREE_PACK = "free_pack"
        const val SILVER_PACK_MONTHLY = "silver_pack_monthly"
        const val SILVER_PACK_ANNUAL = "silver_pack_annual"
        const val GOLDEN_PACK_MONTHLY = "golden_pack_monthly"
        const val GOLDEN_PACK_ANNUAL = "golden_pack_annual"

        const val APP_TYPE = "android"

    }
}