var users = require('../models/users');
var contact = require('../models/contact');

var appRouter = function (app) {

    app.get("/", function (req, res) {
        res.status(200).send("Welcome to RESTFUL API - NODEJS - TINK39");
    });

    app.post("/login", function (req, res) {
        var dt = {
            email: req.body.email,
            password: req.body.password
        }
        users.Login(dt, function (ret) {
            if (ret === 1) {
                res.status(200).send("1");
            } else if (ret === -1) {
                res.status(404).send("404");
            } else if (ret === -101) {
                res.status(-1).send("-1");
            }
        });
    });

    app.post("/register", function (req, res) {
        var data = {
            fullname: req.body.fullname,
            password: req.body.password,
            confirm: req.body.confirm,
            phonenumber: req.body.phonenumber,
            email: req.body.email
        }

        users.AddUser(data, function (ret) {
            console.log('AddUser() => ' + ret);
            if (ret === -1) {
                res.status(200).send("-1");
            }
            if (ret === -2) {
                res.status(200).send("-2");
            }
            if (ret === -3) {
                res.status(200).send("-3");
            }
            if (ret === -4) {
                res.status(200).send("-4");
            }
            if (ret === -5) {
                res.status(200).send("-5");
            }
            if (ret === -6) {
                res.status(200).send("-6");
            }
            if (ret === -7) {
                res.status(200).send("-7");
            }
            if (ret === -101) {
                res.status(200).send("-101");
            }
            if (ret === 1) {
                res.status(200).send("1");
            }
        });
    });

    app.post("/getinfo", function (req, res) {
        var dt = {
            email: req.body.email,
            password: req.body.password
        }
        users.GetInfo(dt, function (ret) {
            console.log('GetInfo() => ' + ret);
            if (!isNaN(ret)) {
                if (ret === -101) {
                    res.status(200).send("-101");
                } else if (ret === -1) {
                    res.status(200).send("-1");
                }
            }
            else {
                res.status(201).send(ret);
            }
        });
    });

    app.post("/addcontact", function (req, res) {
        var ct = {
            owner_email: req.body.owner_email,
            contact_fullname: req.body.contact_fullname,
            contact_phonenumber: req.body.contact_phonenumber,
            contact_address: req.body.contact_address,
            contact_email: req.body.contact_email
        }
        console.log(ct);
        contact.AddContact(ct, function (ret) {
            console.log('add contact :' + ret);
            res.status(200).send('' + ret);
        });
    });
    app.post("/edituser", function (req, res) {
        var eu = {
            fullname: req.body.fullname,
            newpassword: req.body.newpassword,
            oldpassword: req.body.oldpassword,
            email: req.body.email,
            phonenumber: req.body.phonenumber
        }
        users.EditInfo(eu, function (ret) {
            res.status(200).send('' + ret);
        })
    });
    app.post("/listcontact", function (req, res) {
        var oe = {
            owner_email: req.body.owner_email
        }
        console.log("Lay list contact cua: " + oe.owner_email);
        contact.ListContact(oe, function (ret) {
            console.log(ret);
            if (isNaN(ret)) {
                if (ret) {
                    res.status(201).send(ret);
                }
            } else {
                if (ret === -101) {
                    res.status(200).send('-1');
                }
                if (ret === 404) {
                    res.status(200).send('-2');
                }
            }
        })
    });
    app.post("/editcontact", function (req, res) {
        var ec = {
            _id: req.body._id,
            contact_email: req.body.contact_email,
            contact_address: req.body.contact_address,
            contact_fullname: req.body.contact_fullname,
            contact_phonenumber: req.body.contact_phonenumber
        }
        contact.EditContact(ec, function (ret) {
            res.status(200).send('' + ret);
        })
    });
    app.post("/getinfocontact", function (req, res) {
        var gic = {
            _id: req.body._id
        }
        contact.GetInfo(gic, function (ret) {
            if (isNaN(ret)) {
                res.status(200).send('' + ret);
            } else {
                res.status(201).send('' + ret);
            }
        })
    });
    app.post("/deletecontact", function (req, res) {
        var dc = {
            _id: req.body._id
        }
        contact.DeleteContact(dc, function (ret) {
            res.status(200).send('' + ret);
        })
    });
    app.post("/searchname", function (req, res) {
        var sn = {
            owner_email: req.body.owner_email,
            contact_fullname: req.body.contact_fullname
        }
        contact.SearchName(sn, function (ret) {
            if (isNaN(ret)) {
                res.status(201).send(ret);
            } else {
                res.status(200).send('' + ret);
            }
        })
    })
}

module.exports = appRouter;