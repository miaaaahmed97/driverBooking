
import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp();

exports.sendOfferNotification = functions.database.ref('/Offer/{tripID}/{phoneNumber}')
.onCreate((snapshot, context) =>{

  // Grab the current value of what was written to the Realtime Database.

  const original = snapshot.val();
  console.log('tripID', context.params.tripID, original);

  const receiver = snapshot.child("token_id").val();
  console.log('customer', receiver);

  //receiver.getUser.getDeviceTokensPromise

  //const getReceiverUidPromise = admin.auth().getUser(receiver);

  const payload = {
    notification: {
      title: 'New message',
      body: 'Hello'
    }
  };

  return admin.messaging().sendToDevice(receiver, payload).then(result => {
    
    console.log("notification sent");

  });
})
