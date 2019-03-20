const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.sendNotifications = functions.database.ref('/messages/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

  console.log(user_name, ' has a new message');
  return true;
});

exports.sendLendRequestNotification = functions.database.ref('/requests/lendRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

  console.log(user_name, ' has a new lend Request');
  return true;
});

exports.sendBorrowRequestNotification = functions.database.ref('/requests/borrowRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

  console.log(user_name, ' has a new borrow Request');
  return true;
});
