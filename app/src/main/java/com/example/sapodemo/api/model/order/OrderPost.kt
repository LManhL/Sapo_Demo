package com.example.sapodemo.api.model.order


import com.example.sapodemo.model.Order
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class OrderPost(){
    @Json(name = "account_id")
    var accountId: Int? = null
    @Json(name = "allow_no_refund_order_exchange_amount")
    var allowNoRefundOrderExchangeAmount: Boolean? = null
    @Json(name = "assignee_id")
    var assigneeId: Int? = null
    @Json(name = "billing_address")
    var billingAddress: Any? = null
    @Json(name = "business_version")
    var businessVersion: Int? = null
    @Json(name = "cancelled_on")
    var cancelledOn: Any? = null
    @Json(name = "channel")
    var channel: Any? = null
    @Json(name = "code")
    var code: String? = null
    @Json(name = "completed_on")
    var completedOn: Any? = null
    @Json(name = "contact_id")
    var contactId: Any? = null
    @Json(name = "create_invoice")
    var createInvoice: Boolean? = null
    @Json(name = "created_on")
    var createdOn: String? = null
    @Json(name = "customer_data")
    var customerData: Any? = null
    @Json(name = "customer_id")
    var customerId: Int? = null
    @Json(name = "delivery_fee")
    var deliveryFee: Any? = null
    @Json(name = "discount_items")
    var discountItems: List<Any?>? = null
    @Json(name = "discount_reason")
    var discountReason: Any? = null
    @Json(name = "einvoice_status")
    var einvoiceStatus: String? = null
    @Json(name = "email")
    var email: Any? = null
    @Json(name = "expected_delivery_provider_id")
    var expectedDeliveryProviderId: Any? = null
    @Json(name = "expected_delivery_type")
    var expectedDeliveryType: Any? = null
    @Json(name = "expected_payment_method_id")
    var expectedPaymentMethodId: Any? = null
    @Json(name = "finalized_on")
    var finalizedOn: Any? = null
    @Json(name = "finished_on")
    var finishedOn: Any? = null
    @Json(name = "from_order_return_id")
    var fromOrderReturnId: Any? = null
    @Json(name = "fulfillment_status")
    var fulfillmentStatus: String? = null
    @Json(name = "fulfillments")
    var fulfillments: List<Any?>? = null
    @Json(name = "id")
    var id: Int? = null
    @Json(name = "interconnection_status")
    var interconnectionStatus: Any? = null
    @Json(name = "issued_on")
    var issuedOn: String? = null
    @Json(name = "location_id")
    var locationId: Int? = null
    @Json(name = "modified_on")
    var modifiedOn: String? = null
    @Json(name = "note")
    var note: Any? = null
    @Json(name = "order_coupon_code")
    var orderCouponCode: Any? = null
    @Json(name = "order_discount_amount")
    var orderDiscountAmount: Int? = null
    @Json(name = "order_discount_rate")
    var orderDiscountRate: Int? = null
    @Json(name = "order_discount_value")
    var orderDiscountValue: Int? = null
    @Json(name = "order_line_items")
    var orderLineItemPosts: MutableList<OrderLineItemPost> = mutableListOf()
    @Json(name = "order_return_exchange")
    var orderReturnExchange: Any? = null
    @Json(name = "order_returns")
    var orderReturns: List<Any?>? = null
    @Json(name = "packed_status")
    var packedStatus: String? = null
    @Json(name = "payment_status")
    var paymentStatus: String? = null
    @Json(name = "phone_number")
    var phoneNumber: Any? = null
    @Json(name = "prepayments")
    var prepayments: List<Any?>? = null
    @Json(name = "price_list_id")
    var priceListId: Int? = null
    @Json(name = "print_status")
    var printStatus: Boolean? = null
    @Json(name = "process_status_id")
    var processStatusId: Any? = null
    @Json(name = "promotion_redemptions")
    var promotionRedemptions: List<Any?>? = null
    @Json(name = "reason_cancel_id")
    var reasonCancelId: Any? = null
    @Json(name = "received_status")
    var receivedStatus: String? = null
    @Json(name = "reference_number")
    var referenceNumber: Any? = null
    @Json(name = "reference_url")
    var referenceUrl: Any? = null
    @Json(name = "return_status")
    var returnStatus: String? = null
    @Json(name = "ship_on")
    var shipOn: Any? = null
    @Json(name = "ship_on_max")
    var shipOnMax: Any? = null
    @Json(name = "ship_on_min")
    var shipOnMin: Any? = null
    @Json(name = "shipping_address")
    var shippingAddress: Any? = null
    @Json(name = "source_id")
    var sourceId: Int? = null
    @Json(name = "status")
    var status: String? = null
    @Json(name = "tags")
    var tags: List<Any?>? = null
    @Json(name = "tax_treatment")
    var taxTreatment: String? = null
    @Json(name = "tenant_id")
    var tenantId: Int? = null
    @Json(name = "total")
    var total: Int? = null
    @Json(name = "total_discount")
    var totalDiscount: Int? = null
    @Json(name = "total_order_exchange_amount")
    var totalOrderExchangeAmount: Any? = null
    @Json(name = "total_tax")
    var totalTax: Int? = null

    constructor(order: Order): this(){
        this.sourceId = order.sourceId
        this.status = order.status
        this.orderLineItemPosts.addAll(convertOrderLineItemModelListToOrderLineItemPost(order))
    }
    private fun convertOrderLineItemModelListToOrderLineItemPost(order: Order): MutableList<OrderLineItemPost>{
        val tmpList = mutableListOf<OrderLineItemPost>()
        order.orderLineItems.forEach { item ->
            tmpList.add(OrderLineItemPost(item))
        }
        return tmpList
    }
}