const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.sendNotifications = functions.database.ref('/messages/{user_name}/{notification_id}/from').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification_id = context.params.notification_id;
  console.log(user_name, ' has a new message');

  const from = admin.database().ref(`/messages/${user_name}/${notification_id}/from`).once('value');
  return from.then((result) =>{
    const from_user = result.val();
    if(user_name !== from_user){
      const deviceToken = admin.database().ref(`/deviceToken/${user_name}`).once('value');
      return deviceToken.then((result)=> {
        const token_id = result.val();
        const payload = {
          notification: {
            title: "New Message!",
            body: `Message from ${from_user}`,
            icon: "default"
          }
        };
        return admin.messaging().sendToDevice(token_id, payload).then((response) =>{
          return console.log(token_id);
        });
      });

    }else{
      return console.log(user_name, ' Sent the message');
    }
  });
});

exports.sendLendRequestNotification = functions.database.ref('/requests/lendRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

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

  });
});

exports.sendBorrowRequestNotification = functions.database.ref('/requests/borrowRequest/{user_name}/{notification_id}').onWrite((change, context)=>{
  const user_name = context.params.user_name;
  const notification = context.params.notification_id;

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

  });
});
