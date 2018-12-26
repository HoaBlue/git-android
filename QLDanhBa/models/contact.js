var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var contactSchema = new Schema({
    owner_email: String,
    contact_fullname: String,
    contact_phonenumber: String,
    contact_address: String,
    contact_email: String
});

var Contact = mongoose.model('Contacs', contactSchema);

module.exports = Contact;

Contact.AddContact = function (data, callback) {
    //console.log(data);
    if (!data.contact_fullname || data.contact_fullname.length == 0) {
        data.contact_fullname = "Contact";
    }
    if (!data.contact_phonenumber || data.contact_phonenumber.length == 0) {
        return callback(-1);
    }
    if (!data.contact_email || data.contact_email.length == 0) {
        return callback(-2);
    }

    var new_contact = new Contact;
    new_contact.owner_email = data.owner_email;
    new_contact.contact_fullname = data.contact_fullname;
    new_contact.contact_phonenumber = data.contact_phonenumber;
    new_contact.contact_address = data.contact_address;
    new_contact.contact_email = data.contact_email;

    new_contact.save(function (err) {
        if (err) return callback(-101);
        else {
            console.log('Contact save successfully');
            return callback(1);
        }
    });
}
Contact.ListContact = function (data, callback) {
    Contact.find({ owner_email: data.owner_email }, function (err, _doct) {
        if (err) return callback(-101);
        var count = Object.keys(_doct).length;
        //console.log("ket qua: " + Object.keys(_doct).length);
        if (count != 0) {
            return callback(_doct);
        } else {
            return callback(404);
        }
    });
}
Contact.SearchName = function (data, callback) {
    console.log(data);
    Contact.find({ owner_email: data.owner_email, contact_fullname: { $regex: data.contact_fullname } }, function (err, doc) {
        if (err) return callback(-101);
        var count = Object.keys(doc).length
        if (count != 0) {
            return callback(doc);
        } else {
            return callback(404);
        }
    })
}
Contact.EditContact = function (data, callback) {
    Contact.updateOne({ _id: data._id }, {
        $set: {
            contact_email: data.contact_email,
            contact_address: data.contact_address,
            contact_fullname: data.contact_fullname,
            contact_phonenumber: data.contact_phonenumber
        }
    }, function (err, doct) {
        if (err)
            return callback(-101);
        console.log(doct);
        if (doct) {
            if (doct.nModified != 0)
                return callback(1);
            else {
                return callback(-1);
            }
        }
        else return callback(-1);
    })
}
Contact.GetInfo = function (data, callback) {
    Contact.findOne({ _id: data._id }, function (err, doc) {
        if (err) return callback(-101);
        if (doc) {
            return callback(doc);
        } else {
            return callback(-1);
        }
    })
}
Contact.DeleteContact = function (data, callback) {
    Contact.deleteOne({ _id: data._id }, function (err, doc) {
        if (err)
            return callback(-101);
        if (doc) {
            if (doc.nModified != 0) {
                return callback(1);
            }
            else return callback(-1);
        } else {
            return callback(-1);
        }
    })
}
