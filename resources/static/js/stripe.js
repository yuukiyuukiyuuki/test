const stripe = Stripe('pk_test_51PACGEEidPDeDeb4a3ociLz40wnHXz680owe6AsW215y2S1uKSnrkF5YjuMAfYgDpELMIAZXgr9q6Buxch1beJnA00xc6Hvqs7');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });
 