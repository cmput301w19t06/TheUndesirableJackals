const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.sendNotifications = functions.database.ref('/messages/{user_name}/{notification_id}/from/{from_user}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;
  const from_user = context.params.from_user;

  console.log(user_name, ' has a new message');
  if(user_name !== from_user){
    const payload = {
      notification: {
        title: "New Message!",
        body: "Received message",
        icon: "default"
      }
    };
  }
  return true;
});

exports.sendLendRequestNotification = functions.database.ref('/requests/lendRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;
  const borrower = context.params.borrower;

  console.log(user_name, ' has a new lend Request');

  const deviceToken = admin.database().ref(`/deviceToken/${user_name}`).once('value');
  return deviceToken.then((result)=> {
      const token_id = result.val();
      const payload = {
        notification: {
          title: "New Lend Request!",
          body: "Received update",
          icon: "default"
        }
      };
      return admin.messaging().sendToDevice(token_id, payload).then((response)=>{
        return console.log(token_id);
      });

  })
});

exports.sendBorrowRequestNotification = functions.database.ref('/requests/borrowRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;
  const owner = context.params.owner;

  console.log(user_name, ' has a new borrow Request');

  const deviceToken = admin.database().ref(`/deviceToken/${user_name}`).once('value');
  return deviceToken.then((result)=> {
    const payload = {
      notification: {
        title: "New Borrow Request Status!",
        body: "Received update",
        icon: "default"
      }
    };
    const token_id = result.val();
    return admin.messaging().sendToDevice(token_id, payload).then((response)=>{
      return console.log(token_id);
    });

  })
});
