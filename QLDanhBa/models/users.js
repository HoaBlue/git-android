var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var userSchema = new Schema({
    fullname: String,
    email: String,
    phonenumber: String,
    password: String
});

// the schema is useless so far
// we need to create a model using it
var User = mongoose.model('Users', userSchema);

// make this available to our users in our Node applications
module.exports = User;

//
User.AddUser = function (data, callback) {
    if (!data) return callback(-1);
    if (!data.fullname || data.fullname.length < 3) return callback(-2);
    if (!data.phonenumber || data.phonenumber.length < 3) return callback(-3);
    if (!data.password || data.password.length < 6) return callback(-4);
    if (!data.confirm || data.confirm != data.password) return callback(-5);
    if (!data.email || data.email.length < 3) return callback(-7);
    console.log(data);
    User.findOne({ email: data.email }, function (err, _doc) {
        if (err) return callback(-101);

        if (_doc) {
            console.log(_doc.email + ' da ton tai');
            return callback(-6);
        }
        var new_user = new User;
        new_user.fullname = data.fullname;
        new_user.password = data.password;
        new_user.email = data.email;
        new_user.phonenumber = data.phonenumber;
        new_user.save(function (err) {
            if (err) return callback(-101);
            else {
                console.log('User saved successfully!');
                return callback(1);
            }
        });
    });
}
User.Login = function (data, callback) {
    console.log(data);
    User.findOne({ email: data.email, password: data.password }, function (err, _doc) {
        if (err)
            return callback(-101);
        if (_doc) {
            //console.log("email: " + _doc.email);
            return callback(1);
        }
        return callback(-1);
    });
}
User.GetInfo = function (data, callback) {
    User.findOne({ email: data.email, password: data.password }, function (err, _doc) {
        if (err)
            return callback(-101);
        if (_doc) {
            //console.log("email: " + _doc.email);
            return callback(_doc);
        }
        return callback(-1);
    });
}
User.EditInfo = function (data, callback) {
    User.findOne({ email: data.email, password: data.oldpassword }, function (err, doct) {
        if (err) return callback(-101);
        if (doct) {
            var password = data.oldpassword;
            var code = 1;
            if (data.newpassword && data.newpassword.length != 0) {
                password = data.newpassword;
                code = 2;
            }
            User.updateOne({ email: data.email }, { $set: { fullname: data.fullname, password: password, phonenumber: data.phonenumber } }, function (err, _doc) {
                if (err) return callback(-101);
                if (_doc) {
                    if (_doc.nModified != 0)
                        return callback(code);
                    else
                        return callback(2);
                }
                return callback(-1);
            });
        } else {
            return callback(-2);
        }
    });
}