const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.sendNotifications = functions.database.ref('/notifications/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

  console.log('The user name is : ', user_name);
  return true;
});

exports.sendBookRequestNotification = functions.database.ref('/requests/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

  console.log('The user name is : ', user_name);
  return true;
});
