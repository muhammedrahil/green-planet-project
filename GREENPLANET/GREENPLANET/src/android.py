import requests
from flask import *
from src.dbconnector import *
app=Flask(__name__)


@app.route("/logincode",methods=['post'])
def logincode():
    uname=request.form['username']
    password=request.form['password']
    q="select * from login where username=%s and password=%s "
    val=uname,password
    res=selectone(q,val)
    if res is None:
        return jsonify({'task': 'invalid'})
    else:
        return jsonify({'task': 'success','lid':res[0],'type':res[3]})



@app.route("/register",methods=['post'])
def register():
    print(request.form)
    fname=request.form['fname']
    lname=request.form['lname']
    Gender=request.form['Gender']
    Phone=request.form['Phone']
    Place=request.form['Place']
    Post=request.form['Post']
    Pin=request.form['Pin']
    Email=request.form['Email']
    username=request.form['username']
    password=request.form['password']

    qry = "insert into login values(null,%s,%s,'customer')"
    value = (username, password)
    lid = iud(qry, value)
    qry1 = "insert into customer values(null,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    val = (fname, lname, Gender, Phone,Place,Post,Pin,Email,str(lid))
    iud(qry1, val)

    return jsonify({'task': 'success'})





@app.route('/add_complaint',methods=['post'])
def add_complaint():
    Customer_id=request.form['Customer_id']
    Complaint=request.form['Complaint']
    qry="insert into complaint values(null,%s,%s,curdate(),'pending')"
    val=(Complaint,Customer_id,)
    iud(qry,val)

    return jsonify({'task': 'success'})


@app.route('/feedback',methods=['post'])
def feedback():
    Customer_id=request.form['Customer_id']
    fd=request.form['fd']
    qry="insert into feedback values(null,curdate(),%s,%s)"
    val=(Customer_id,fd)
    iud(qry,val)

    return jsonify({'task': 'success'})




@app.route('/view_replay',methods=['post'])
def view_replay():
    Customer_id = request.form['lid']
    print(Customer_id)
    qry="SELECT * FROM `complaint` WHERE `userid`=%s"
    val=(Customer_id)

    result=androidselectall(qry,val)

    return jsonify(result)


@app.route('/view_pickup_point',methods=['post'])
def view_pickup_point():
    qry="select * from `assign_area`"
    res=androidselectallnew(qry)
    print(res)
    return jsonify(res)

@app.route('/add_pickup_point',methods=['post'])
def add_pickup_point():

    request_id = request.form['request_id']
    amount=request.form['amount']

    qry="insert into pickup_report values (null,%s,%s)"
    val=(request_id,amount)
    iud(qry, val)

    return jsonify({'task': 'success'})




@app.route('/view_pickup_point1',methods=['post'])
def view_pickup_point1():
    qry="SELECT `assign_area`.`Date`,`assign_area`.`Latitude`,`assign_area`.`Longitude`,`assign_area`.`Location`,`vehicle`.`Vehicle_no` FROM `vehicle` JOIN `assign_area` ON `assign_area`.`Vehicle_id`=`vehicle`.`Vehicle_id` "

    result=androidselectallnew(qry)
    return jsonify(result)




@app.route('/send_request',methods=['post'])
def send_request():
    Customer_id=request.form['Customer_id']
    Assignarea_id=request.form['aid']
    waste_type=request.form['waste_type']
    lati=request.form['lati']
    longi=request.form['longi']
    qry="insert into user_request values(null,%s,%s,%s,'pending',curdate(),'pending',%s,%s)"
    val=(Customer_id,Assignarea_id,waste_type,lati,longi)
    id=iud(qry,val)
    qry1="insert into bill values(null,%s,'0',curdate(),0,%s)"
    val1=(str(id),waste_type)
    iud(qry1,val1)
    return jsonify({'task': 'success'})

@app.route('/cash_entry',methods=['post'])
def cash_entry():
    Request_id=request.form['Request_id']
    userid=request.form['Customer_id']
    Amount=request.form['Amount']
    status=request.form['status']
    qty=request.form['qty']

    qry="update  bill set Amount=%s,Quantity=%s where Request_id=%s"

    val=(Amount,qry,Request_id)
    iud(qry,val)

    qry1="update user_request set Status=%s,quantity=%s where Request_id=%s"
    val1=(status,qty,Request_id)
    iud(qry1,val1)


    return jsonify({'task': 'success'})






@app.route('/view_waste_bill',methods=['post'])
def view_waste_bill():
    u_id=request.form['uid']
    print(u_id)
    wtype=request.form['wtype']


    qry="SELECT `bill`.* ,`user_request`.*FROM `bill` JOIN `user_request` ON `user_request`.`Request_id`=`bill`.`Request_id` WHERE `user_request`.`Customer_id`=%s and `user_request`.`waste_type`=%s"
    val=(u_id,wtype)

    result=androidselectall(qry,val)
    print(result)
    return jsonify(result)

@app.route('/agent_bill',methods=['post'])
def agent_bill():
    u_id=request.form['Vid']
    print(u_id)

    qry="SELECT `bill`.* ,`user_request`.*,`customer`.* FROM `bill` JOIN `user_request` ON `user_request`.`Request_id`=`bill`.`Request_id` JOIN `assign_area` ON `assign_area`.`Area_id`=`user_request`.`Assignarea_id`  JOIN `customer` ON `customer`.`customer_loginid`=`user_request`.`Customer_id` WHERE `assign_area`.`Vehicle_id`=%s and `user_request`.`Status`='pending'"
    val=(u_id)

    result=androidselectall(qry,val)
    print(result)
    return jsonify(result)



@app.route('/view_product_bill',methods=['post'])
def view_product_bill():
    u_id=request.form['uid']
    print(u_id)

    qry="select `product_bill`.* from `product_bill` join `user_request` on `user_request`.`Customer_id`=`product_bill`.`userid` where `product_bill`.`userid`=%s group by `product_bill`.`billid`"
    val=(u_id)

    result=androidselectall(qry,val)
    print(result)
    return jsonify(result)


@app.route('/purstart',methods=['get','post'])
def purstart():
    Uid=request.form['uid']
    qry="INSERT INTO`product_bill` VALUES(NULL,%s,CURDATE(),0,'pending')"
    value=(Uid)
    id=iud(qry,value)
    print(id)
    return jsonify({'task': str(id)})

@app.route('/view_more_bill_items')
def view_more_bill_items():
    bill_id=request.form['bill_id']
    qry="SELECT `product`.* FROM `product`JOIN `booking`ON `booking`.`Product_id`=`product`.`Product_id` WHERE `booking`.`product_billid`=1"
    val=(bill_id)

    result = androidselectallnew(qry, val)
    return jsonify(result)



@app.route('/attend_training_class',methods=['post'])
def  attend_training_class():

    qry="select * from organize_class"

    result = androidselectallnew(qry)
    return jsonify(result)



@app.route('/view_product',methods=['post'])
def view_product():

    qry="select * from product"

    result = androidselectallnew(qry)
    return jsonify(result)



@app.route('/order_product',methods=['post'])
def order_product():

    Amount=request.form['Amount']
    Customer_id=request.form['Customer_id']
    Product_id=request.form['Product_id']
    billid=request.form['billid']

    qry="insert into booking values(null,%s,%s,%s,%s,curdate())"
    val=(billid,Product_id,Customer_id,Amount)
    iud(qry,val)
    return jsonify({'task': 'success'})


@app.route('/finish',methods=['post'])
def finish():

    Customer_id=request.form['Customer_id']
    billid=request.form['billid']
    qry="select SUM(`Amount`) from `booking` where `billid`=%s"
    val=(billid)
    s=selectone(qry,val)
    amt=s[0]

    qry="update product_bill set userid=%s,date=curdate(),amount=%s where billid=%s"
    val=(Customer_id,str(amt),billid)
    iud(qry,val)
    return jsonify({'task': 'success'})


@app.route('/vehicle_login',methods=['post'])
def vehicle_login():
    uname = request.form['username']
    password = request.form['password']
    q = "select * from login where username=%s and password=%s"
    val = uname, password
    res = selectone(q, val)
    if res is None:
        return jsonify({'task': 'invalid'})
    else:
        return jsonify({'task': 'success', 'lid': res[0], 'type': res[3]})


@app.route('/view_route',methods=['post'])
def view_route():
    id=request.form['lid']
    qry="select * from assign_area where Vehicle_id=%s"
    val=id
    result = androidselectall(qry,val)
    return jsonify(result)



@app.route('/billing',methods=['post'])
def billing():
    qry="SELECT `bill`.*,`user_request`.`Date`, `user_request`.`Status` FROM `bill`JOIN `user_request` ON `user_request`.`Request_id` = `bill`.`Request_id`"
    result = androidselectallnew(qry)
    return jsonify(result)




@app.route('/view_user_resquest',methods=['post'])
def view_user_resquest():
    vehicle_id=request.form['lid']
    print(vehicle_id)
    qry="select `customer`.`Fname`,`customer`.`Lname`,`customer`.`Phone`,`user_request`.* from `user_request`join `customer`on`customer`.`login_id`=`user_request`.`Customer_id`join `assign_area`on`assign_area`.`Area_id`=`user_request`.`Assignarea_id` where `assign_area`.`Vehicle_id`=%s and `user_request`.`Status`='pending'"
    val=(vehicle_id)
    result = androidselectall(qry,val)
    print(result)
    return jsonify(result)

@app.route('/View_my_request',methods=['post'])
def View_my_request():
    user_id=request.form['user_id']
    print(user_id)
    qry="select * from user_request where Customer_id=%s"
    val=(user_id)
    result = androidselectall(qry,val)
    return jsonify(result)

@app.route('/send_comp',methods=['post'])
def send_comp():
    Customer_id=request.form['Customer_id']
    Complaint=request.form['Complaint']

    qry="insert into enquiry values(null,%s,%s,curdate(),'pending')"
    val=(Customer_id,Complaint)
    iud(qry,val)

    return jsonify({'task': 'success'})

@app.route('/requestwork',methods=['post'])
def requestwork():
    Customer_id=request.form['uid']
    wid=request.form['wid']

    qry="insert into request_to_kudumbasree values(null,%s,curdate(),%s,'pending')"
    val=(Customer_id,wid)
    iud(qry,val)

    return jsonify({'task': 'success'})

@app.route('/view_notification',methods=['post'])
def view_notification():
    qry="select * from notification"
    result = androidselectallnew(qry)
    return jsonify(result)

@app.route('/viewworkk',methods=['post'])
def viewworkk():
    qry="select * from kudubasree_work"
    result = androidselectallnew(qry)
    return jsonify(result)

@app.route('/view_product1',methods=['post'])
def view_product1():
    bid=request.form['bid']
    print(bid)
    qry="SELECT `product`.* FROM `booking` JOIN `product`  ON `product`.`Product_id`=`booking`.`pid` WHERE `booking`.`billid`=%s"
    val=(bid)
    result = androidselectall(qry,val)
    return jsonify(result)

@app.route('/update_loc')
def update_loc():
    Vehicle_id=request.form['Vehicle_id']
    Latitude=request.form['Latitude']
    Longitude=request.form['Longitude']
    qry="select * from location where Vehicle_id=%s"
    val=(Vehicle_id)
    res=selectone(qry,val)
    if res is None:
        qry1="insert into location values(null,%s,%s,%s)"
        val1=(Vehicle_id,Latitude,Longitude)
        iud=(qry1,val1)

        return jsonify({'task': 'success'})
    else:
        qry="update location set Latitude=%s,Longitude=%s where Vehicle_id=%s"
        val=(Latitude,Longitude,Vehicle_id)
        iud = (qry, val)

        return jsonify({'task': 'success'})

@app.route('/viewwork',methods=['post'])
def viewwork():
    vid=request.form['vid']
    print(vid)
    qry="SELECT `product_bill`.*,`customer`.* FROM `product_bill` JOIN `customer` ON `customer`.`customer_loginid`=`product_bill`.`userid` JOIN `assignwork` ON `assignwork`.`product_bill_id`=`product_bill`.`billid` WHERE `assignwork`.`vehicle_id`=%s"
    val=(vid)
    result = androidselectall(qry,val)
    return jsonify(result)


@app.route('/viewalll',methods=['post'])
def viewalll():
    vid=request.form['Vid']
    print(vid)
    qry="SELECT `product_bill`.* ,`customer`.`Fname`,`customer`.`Lname`FROM `customer` JOIN `product_bill` ON `product_bill`.`userid`=`customer`.`customer_loginid` JOIN `assignwork` ON `assignwork`.`product_bill_id`=`product_bill`.`billid` WHERE `assignwork`.`vehicle_id`=%s"
    val=(vid)
    result = androidselectall(qry,val)
    print(result)
    return jsonify(result)


@app.route('/viewallls',methods=['post'])
def viewallls():
    vid=request.form['bid']
    print(vid)
    qry="SELECT `product`.*,`product_bill`.* FROM `product_bill` JOIN `booking` ON `booking`.`product_billid`=`product_bill`.`billid` JOIN `product` ON `product`.`Product_id`=`booking`.`Product_id` WHERE `booking`.`product_billid`=%s"
    val=(vid)
    result = androidselectall(qry,val)
    return jsonify(result)



@app.route('/update_status',methods=['post'])
def update_status():
    billid=request.form['bid']
    status=request.form['status']

    qry="update product_bill set status=%s where billid=%s"
    val=(status,billid)
    iud(qry,val)
    return jsonify({'task': 'success'})
@app.route('/view_pricechart',methods=['post'])
def view_pricechart():
    qry="select * from pricechart"

    result=androidselectallnew(qry)
    return jsonify(result)


@app.route('/view_user_resquest_work',methods=['post'])
def view_user_resquest_work():
    print(request.form)
    lid=request.form['lid']

    qry="SELECT `kudubasree_work`.*,`request_to_kudumbasree`.* FROM `request_to_kudumbasree` JOIN `kudubasree_work` ON `kudubasree_work`.`id`=`request_to_kudumbasree`.`workid` WHERE `request_to_kudumbasree`.`ulid`=%s"
    val=(lid)
    result = androidselectall(qry,val)
    return jsonify(result)


app.run(host="0.0.0.0",port=5000)


















