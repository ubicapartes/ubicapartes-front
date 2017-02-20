function tokenizeCard(cardholderName, number, expirationMonth, expirationYear, cvv){
	client.tokenizeCard({
		number: number,
		cardholderName: cardholderName,
		// You can use either expirationDate
		//expirationDate: "10/20",
		// or expirationMonth and expirationYear
		expirationMonth: expirationMonth,
		expirationYear: expirationYear,
		// CVV if required
		cvv: cvv,
	}, function (err, nonce) {
		// Send nonce to your server
		var event = null;
		var data = null;
		if(!err){
			event = "onPaymentSuccess";
			data = {payment_method_nonce : nonce};
		}
		else {
			event = "onPaymentError";
		}
		zk.Widget.$('$cmpPayment').fire(event, data, {toServer:true});
	});
};