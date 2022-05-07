import os
from flask import *
from werkzeug.utils import secure_filename


import pymysql

from src.dbconnector import *

app=Flask(__name__)
app.secret_key="323"


import functools
def login_required(func):
    @functools.wraps(func)
    def secure_function():
        if "lid" not in session:
            return redirect ("/")
        return func()
    return secure_function

@app.route('/')
def login():
    return render_template("accountlogin.html")


@app.route('/main',methods=['post'])
def main():
    uname=request.form['textfield']
    password=request.form['textfield2']
    qry="select *  from login where Username=%s and Password=%s"
    val=(uname,password)
    res=selectone(qry,val)
    if res is None:
        return '''<script>alert(" inavlid username or password ");window.location='/'</script>'''
    elif res[3]=='admin':
        session['lid']=res[0]
        return '''<script>alert(" login successfull ");window.location='/adminhome'</script>'''
    elif res[3] == 'KUDUBASREE':
        session['lid']=res[0]
        return '''<script>alert(" login successfull ");window.location='/kudumbhome'</script>'''
    elif res[3] == 'company':
        session['lid']=res[0]
        return '''<script>alert(" login successfull ");window.location='/COMPANYhome'</script>'''


    else:
        return '''<script>alert(" invalid ");window.location='/'</script>'''



@app.route('/adminhome')
@login_required

def adminhome():
    return render_template("ADMINHOME.HTML")

@app.route('/logout')
def logout():
    session.clear()
    return render_template("accountlogin.html")

@app.route('/COMPANYhome')


def COMPANYhome():
    return render_template("COMPANYHOME.HTML")

@app.route('/authorityhome')
@login_required

def authorityhome():
    return render_template("authorityhome.html")


@app.route('/kudumbhome')
@login_required

def kudumbhome():
    return render_template("KUDUMB_HOME.html")

@app.route('/kudumbasreereg')
@login_required

def kudumbasreereg():
    return render_template("KUDUMBASREEREG.html")




@app.route('/MANAGECOMPANY')
@login_required

def MANAGECOMPANY():
    q="select * from company_registration"
    s=select(q)
    return render_template("MANAGECOMPANY.HTML",val=s)


@app.route('/KUDUMBASREEREG',methods=['post'])
@login_required

def KUDUMBASREEREG():
    return render_template("KUDUMBASREEREG.html")


@app.route('/VIEWCOMPLAINTS')
@login_required

def VIEWCOMPLAINTS():
    q="SELECT `complaint`.*,`customer`.`Fname`,`customer`.`Lname` FROM `customer` JOIN `complaint` ON `complaint`.`userid`=`customer`.`login_id` WHERE `complaint`.`reply`='pending'"
    s=select(q)
    return render_template("VIEWCOMPLAINTS.HTML",val=s)

@app.route('/SENDREPLY')
@login_required

def SENDREPLY():
    id=request.args.get('id')
    session['cmpid']=id
    return render_template("SENDREPLY.HTML")
@app.route('/reply',methods=['post'])
@login_required

def reply():
    cmp=request.form['textarea']
    q="update complaint set reply=%s where id=%s"
    v=cmp,session['cmpid']
    iud(q,v)
    return '''<script>alert(" replied ");window.location='/VIEWCOMPLAINTS'</script>'''


@app.route('/assignarea')
@login_required

def assignarea():
    q="select * from vehicle where cmpyid=%s"
    res=selectall(q,session['lid'])
    QQ="SELECT `assign_area`.*,`vehicle`.* FROM `vehicle` JOIN `assign_area` ON `vehicle`.`Login_id`=`assign_area`.`Vehicle_id`"
    r=select(QQ)
    return render_template("company/assignarea.html",val=res,val1=r)

@app.route('/custumerdetails')

@login_required
def custumerdetails():
    q="select * from customer"
    res=select(q)
    return render_template("company/custemerdetails.html",val=res)




@app.route('/custumerdetails1')
@login_required

def custumerdetails1():
    q="select * from customer"
    res=select(q)
    return render_template("kudumbasree/custemerdetails.html",val=res)

@app.route('/notification')
@login_required

def notification():
    return render_template("Notification.html")


@app.route('/pickupreport')
@login_required

def pickupreport():
    qry = "SELECT `product_bill`.*,`customer`.`Fname`,`customer`.`Lname` FROM `product_bill` JOIN `customer` ON `product_bill`.`userid`=`customer`.`login_id` where `product_bill`.`status`='pending'  "
    result = select(qry)

    return render_template("company/pickupreport.html",val=result)



@app.route('/publiccomplaints')
@login_required

def publiccomplaints():
    return render_template("publiccomplaints.html")

@app.route('/requeststatusvehicle')
@login_required

def requeststatusvehicle():
    q = "SELECT `customer`.*,`user_request`.*,`assign_area`.*,`vehicle`.*FROM `user_request` JOIN `customer` ON `customer`.`login_id`=`user_request`.`Customer_id` JOIN `assign_area` ON `assign_area`.`Area_id`=`user_request`.`Assignarea_id` JOIN `vehicle` ON `vehicle`.`Login_id`=`assign_area`.`Vehicle_id`"
    res = select(q)
    print(res)

    return render_template("company/VIEREQUESTSTATUS.html",val=res)


@app.route('/vehicleregistration',methods=['post'])
@login_required

def vehicleregistration():
    return render_template("company/VEHICLEREGITRATION.HTML")


@app.route('/vehicleregistration1',methods=['post'])
@login_required

def vehicleregistration1():
    try:
        model=request.form['textfield']
        vehicleno = request.form['textfield2']
        phone = request.form['textfield3']
        username = request.form['textfield4']
        password = request.form['textfield5']
        qry="INSERT INTO `login` VALUES(NULL,%s,%s,'vehicle')"
        val=(username,password)
        id=iud(qry,val)
        qry1="INSERT INTO `vehicle` VALUES(NULL,%s,%s,%s,%s,%s)"
        val1=(model,vehicleno,phone,str(id),session['lid'])
        iud(qry1,val1)
        return'''<script>alert("successfully registered");window.location="/managevehicle"</script>'''
    except Exception as e:
        return'''<script>alert("duplicate entry");window.location="/COMPANYhome"</script>'''




@app.route('/manageclass')
@login_required

def manageclass():
    return render_template("manageclass.html")


@app.route('/organiseclass')
@login_required

def organiseclass():
    return render_template("ORGANISECLASS.HTML")


@app.route('/supplyproduct')
@login_required

def supplyproduct():
    return render_template("SUPPLYPRODUCT.html")

@app.route('/manageproduct')
@login_required

def manageproduct():
    return render_template("manageproduct.html")

@app.route('/confirmbooking')
@login_required

def confirmbooking():
    return render_template("confirmbooking.html")


@app.route('/trainghome')
@login_required

def trainghome():
    return render_template("TRAINGHOME.HTML")


@app.route('/plantvideos')
@login_required

def plantvideos():
    return render_template("PLANTVIDEOS.HTML")

@app.route('/addplantvideos')
@login_required

def addplantvideos():
    return render_template("ADDPLANTVIDEOS.HTML")

@app.route('/addmakingvideos')
@login_required

def addmakingvideos():
    return render_template("ADDMAKINGVIDEOS.HTML")

@app.route('/makingvideos')
@login_required

def makingvideos():
    return render_template("MAKINGVIDEOS.HTML")

@app.route('/feedback')
@login_required

def feedback():
    q="SELECT `feedback`.*,`customer`.`Fname`,`customer`.`Lname` FROM `customer` JOIN `feedback` ON `feedback`.`uid`=`customer`.`login_id`"
    res=select(q)
    return render_template("feedback.html",val=res)


@app.route('/feedback1')
@login_required

def feedback1():
    q="SELECT `feedback`.*,`customer`.`Fname`,`customer`.`Lname` FROM `customer` JOIN `feedback` ON `feedback`.`uid`=`customer`.`login_id`"
    res=select(q)
    return render_template("company/feedback.html",val=res)


@app.route('/AWARNESS')
@login_required

def AWARNESS():
    q="SELECT * FROM `training_video` WHERE STATUS='AWARNESS'"

    res=select(q)
    return render_template("awarnessprogram.HTML",val=res)
@app.route('/mngek')
@login_required

def mngek():
    q="SELECT * FROM kudubasreereg"

    res=select(q)
    return render_template("managkudumbasree.html",val=res)

@app.route('/addAWARNESS',methods=['post'])
@login_required

def addAWARNESS():

    des=request.form['textarea']
    file=request.files['video']
    ff=secure_filename(file.filename)
    file.save(os.path.join('./STATIC/video/',ff))
    q="insert into training_video values(null,%s,%s,'AWARNESS')"
    v=des,ff
    iud(q,v)
    return redirect('/AWARNESS')

@app.route('/delaw')
@login_required

def delaw():
    id=request.args.get('id')
    q="delete from training_video where id=%s"
    res=iud(q,id)
    return redirect('/AWARNESS')


@app.route('/removev')
@login_required

def removev():
    id=request.args.get('id')
    q="delete from assign_area where Area_id=%s"
    res=iud(q,id)
    return redirect('/assignarea')


@app.route('/delnoti')
@login_required

def delnoti():
    id=request.args.get('id')
    q="delete from notification where Notification_id=%s"
    res=iud(q,id)
    return redirect('/addnotification')

@app.route('/addnotification')
@login_required

def addnotification():
    q="SELECT * FROM `notification`"
    res=select(q)
    return render_template("Notification.html",val=res)

@app.route('/training_video')
@login_required

def training_video():
    q="SELECT * FROM `training_video` where status='training'"
    res=select(q)
    return render_template("ADDMAKINGVIDEOS.HTML",val=res)


@app.route('/sentnotification',methods=['post'])
@login_required

def sentnotification():

    noti=request.form['textfield']
    q="insert into notification values(null,%s,curdate())"
    iud(q,noti)
    return redirect('/addnotification#about')
@app.route('/addvideo',methods=['post'])
@login_required

def addvideo():

    des=request.form['textarea']
    file=request.files['video']
    ff=secure_filename(file.filename)
    file.save(os.path.join('./STATIC/video/',ff))
    q="insert into training_video values(null,%s,%s,'training')"
    v=des,ff
    iud(q,v)
    return redirect('/training_video#about')

@app.route('/delvideo')
@login_required

def delvideo():
    id=request.args.get('id')
    q="delete from training_video where id=%s"
    res=iud(q,id)
    return redirect('/training_video#about')

@app.route('/COMPANYREG',methods=['post'])
@login_required

def COMPANYREG():
    return render_template("COMPANYREGISTRATION.HTML")


@app.route('/addcompany',methods=['post'])
@login_required

def addcompany():
    try:
        cname=request.form['textfield']
        place=request.form['textfield2']
        post=request.form['textfield3']
        pin=request.form['textfield4']
        email=request.form['textfield5']
        phone=request.form['textfield6']
        username=request.form['textfield7']
        password=request.form['textfield8']

        query="insert into login values(null,%s,%s,'company')"
        val=(username,password)
        id=iud(query,val)
        query="insert into company_registration values(null,%s,%s,%s,%s,%s,%s,%s)"
        val=(str(id),cname,place,post,pin,phone,email)
        iud(query,val)

        return'''<script>alert("successfully added");window.location="/MANAGECOMPANY"</script>'''
    except Exception as e:
        print(e)
        return '''<script>alert("duplication entry ");window.location="/MANAGECOMPANY"</script>'''






@app.route('/updatec',methods=['post'])
@login_required

def updatec():
    cname=request.form['textfield']
    place=request.form['textfield2']
    post=request.form['textfield3']
    pin=request.form['textfield4']
    email=request.form['textfield5']
    phone=request.form['textfield6']
    query="update company_registration set companyname=%s,place=%s,post=%s,pin=%s,phone=%s,email=%s where  companyloginid=%s"
    val=(cname,place,post,pin,phone,email,session['cid'])
    iud(query,val)

    return'''<script>alert("successfully updated...");window.location="/MANAGECOMPANY"</script>'''


@app.route('/addkudumbasree',methods=['post'])
@login_required

def addkudumbasree():
    kname=request.form['textfield']
    place=request.form['textfield2']
    post=request.form['textfield3']
    pin=request.form['textfield4']
    phone=request.form['textfield5']
    username=request.form['textfield6']
    password=request.form['textfield7']

    query="insert into login values(null,%s,%s,'KUDUBASREE')"
    val=(username,password)
    id=iud(query,val)
    query="insert into kudubasreereg values(null,%s,%s,%s,%s,%s,%s)"
    val=(str(id),kname,place,post,pin,phone)
    iud(query,val)

    return redirect('/mngek')





@app.route('/editkudumbasree',methods=['post'])
@login_required

def editkudumbasree():
    kname=request.form['textfield']
    place=request.form['textfield2']
    post=request.form['textfield3']
    pin=request.form['textfield4']
    phone=request.form['textfield5']
    query="update kudubasreereg set NANE=%s,PLACE=%s,POST=%s,PIN=%s,PHONE=%s WHERE KUDUBASREEID=%s"
    val=(kname,place,post,pin,phone,session['kid'])
    iud(query,val)

    return redirect('/mngek')









@app.route('/editc')
@login_required

def editc():
    id=request.args.get('id')
    session['cid']=id
    q="select * from company_registration  where companyloginid=%s  "
    res=selectone(q,id)
    return render_template("editc.html",val=res)


@app.route('/editk')
@login_required

def editk():
    id=request.args.get('id')
    session['kid']=id
    q="select * from kudubasreereg  where KUDUBASREEID=%s  "
    res=selectone(q,id)
    return render_template("editk.html",val=res)
@app.route('/deletec')
@login_required

def deletec():
    id=request.args.get('id')
    q="delete from login where Loginid=%s"
    res=iud(q,id)
    q1="delete from company_registration where companyloginid=%s"
    res=iud(q1,id)
    return redirect('/MANAGECOMPANY')
@app.route('/deletek')
@login_required

def deletek():
    id=request.args.get('id')
    q="delete from login where Login_id=%s"
    res=iud(q,id)
    q1="delete from kudubasreereg where KUDUBASREEID=%s"
    res=iud(q1,id)
    return redirect('/mngek')

@app.route('/viewuserrequest')
@login_required

def viewuserrequest():
    q="SELECT `customer`.`Fname`,`customer`.`Lname`,`kudubasree_work`.*,`request_to_kudumbasree`.* FROM `request_to_kudumbasree` JOIN `kudubasree_work` ON `kudubasree_work`.`id`=`request_to_kudumbasree`.`workid` JOIN `customer` ON `customer`.`login_id`=`request_to_kudumbasree`.`ulid` WHERE `request_to_kudumbasree`.`status`='pending' and `kudubasree_work`.`k_id`=%s"
    res=selectall(q,session['lid'])
    print(q)
    print(session['lid'])
    return render_template("kudumbasree/VIEREQUEST.html",val=res)
@app.route('/viewwork')
@login_required

def viewwork():
    q="select * from `kudubasree_work` WHERE `k_id`=%s"
    res=selectall(q,session['lid'])
    return render_template("kudumbasree/VIEW_WORK.html",val=res)

@app.route('/removework')
@login_required

def removework():
    id=request.args.get('id')
    q="delete from  kudubasree_work where id=%s"
    iud(q,id)
    return'''<script>alert("successfully removed...");window.location="/viewwork"</script>'''

@app.route('/addwork',methods=['post'])
@login_required

def addwork():
    return render_template("kudumbasree/ADDWORK.HTML")

@app.route('/insertwork',methods=['post'])
@login_required

def insertwork():
    work=request.form['textfield']
    des=request.form['textarea']
    q="insert into kudubasree_work values(null,%s,%s,%s)"
    v=(work,des,session['lid'])
    iud(q,v)
    return'''<script>alert("successfully added...");window.location="/viewwork#about"</script>'''


@app.route('/acceptreq')
@login_required

def acceptreq():
    id=request.args.get('id')
    q="update  request_to_kudumbasree set status='accept' where reqid=%s"
    iud(q,id)
    return'''<script>alert("successfully accepted...");window.location="/viewuserrequest#about"</script>'''


@app.route('/rejectreq')
@login_required

def rejectreq():
    id=request.args.get('id')
    q="update  request_to_kudumbasree set status='reject' where reqid=%s"
    iud(q,id)
    return'''<script>alert("successfully reject...");window.location="/viewuserrequest#about"</script>'''

@app.route('/pricechart')
@login_required

def pricechart():
    q="select * from pricechart "
    res=select(q)
    return render_template("company/pricechart.HTML",val=res)



@app.route('/addpricechart',methods=['post'])
@login_required

def addpricechart():
    type=request.form['select']
    file=request.files['video']
    ff=secure_filename(file.filename)
    file.save(os.path.join('./STATIC/chart/',ff))
    q="select * from pricechart where type=%s"
    rr=selectone(q,type)
    if rr is None:
        q="insert into pricechart values(null,%s,%s,%s)"
        v=type,ff,session['lid']
        iud(q,v)
        return redirect('/pricechart')

    else:
        id=rr[0]
        q="update pricechart set chart=%s where id=%s "
        v=(ff,id)
        iud(q,v)
        return redirect('/pricechart')
@app.route('/delchart')
@login_required

def delchart():
    id=request.args.get('id')
    q="delete from  pricechart where id=%s"
    iud(q,id)
    return'''<script>alert("successfully removed...");window.location="/pricechart"</script>'''


@app.route('/managevehicle')
@login_required

def managevehicle():
    q="select * from vehicle where cmpyid=%s"
    res=selectall(q,session['lid'])
    return render_template("company/managevehicle.html",val=res)

@app.route('/delv')
@login_required

def delv():
    id=request.args.get('id')
    q="delete from login where Login_id=%s"
    res=iud(q,id)
    q1="delete from vehicle where Vehicle_id=%s"
    res=iud(q1,id)
    return redirect('/managevehicle')

@app.route('/wasterequest')
@login_required

def wasterequest():
    q="SELECT `customer`.*,`user_request`.*,`assign_area`.*FROM `user_request` JOIN `customer` ON `customer`.`login_id`=`user_request`.`Customer_id` JOIN `assign_area` ON `assign_area`.`Area_id`=`user_request`.`Assignarea_id` WHERE `user_request`.`Status`='pending'"
    res=select(q)
    print(res)

    return render_template("company/VIEREQUEST.html",val=res)



@app.route('/assignareas',methods=['post'])
@login_required

def assignareas():
    vid=request.form['select']
    lati=request.form['textfield2']
    longi=request.form['textfield3']
    loc=request.form['textfield']
    q="insert into assign_area values(null,%s,curdate(),%s,%s,%s) "
    val=vid,lati,longi,loc
    iud(q,val)
    return'''<script>alert("successfully ADDED...");window.location="/assignarea"</script>'''


@app.route('/aaceptwreq')
@login_required

def aaceptreq():
    id=request.args.get('id')
    q="update `user_request` SET `Status`='accepted by company' WHERE Request_id=%s"
    res=iud(q,id)
    return'''<script>alert("successfully aceepted...");window.location="/wasterequest"</script>'''


@app.route('/rejectwreq')
@login_required

def rejectwreq():
    id=request.args.get('id')
    q="update `user_request` SET `Status`='reject by company' WHERE Request_id=%s"
    res=iud(q,id)
    return'''<script>alert("successfully rejected...");window.location="/wasterequest"</script>'''



@app.route('/manage_products')
@login_required

def manage_products():
    query = "select * from product"
    result = select(query)
    return render_template("company/manage_products.html",val=result)
@app.route('/edit_product')
@login_required

def edit_product():
    id = request.args.get('id')
    session['manage'] = id
    qry = "select * from product where Product_id=%s"
    values = (id)
    result = selectone(qry,values)
    return render_template("company/edit_product.html", s=result)


@app.route('/update_product1',methods=['post'])
@login_required
def update_product1():
    try:
        Name=request.form['textfield']
        Discription=request.form['textarea']
        Amount=request.form['textfield2']
        image = request.files['file']
        file = secure_filename(image.filename)
        image.save(os.path.join("static/files", file))

        qry="update  product set Product_name=%s,Details=%s,Image=%s,Price=%s where Product_id=%s"
        values=(Name,Discription,file,Amount,session['manage'])
        iud(qry,values)
        return '''<script>alert ("Edited succesfully");window.location="/manage_products"</script>'''
    except:
        Name = request.form['textfield']
        Discription = request.form['textarea']
        Amount = request.form['textfield2']
        qry = "update  product set Product_name=%s,Details=%s,Price=%s where Product_id=%s"
        values = (Name, Discription, Amount, session['manage'])
        iud(qry, values)
        return '''<script>alert ("Edited succesfully");window.location="/manage_products"</script>'''


@app.route('/confirm_booking1')
@login_required
def confirm_booking1():
    qry="SELECT `product_bill`.*,`customer`.`Fname`,`customer`.`Lname` FROM `product_bill` JOIN `customer` ON `product_bill`.`userid`=`customer`.`login_id`  WHERE `product_bill`.`status`='pending'"
    result=select(qry)
    return render_template("company/Confirmedbooking.html",val=result)



@app.route('/delete_product')
@login_required
def delete_product():
    id=request.args.get('id')
    qry = "delete from product where Product_id=%s"
    values = (id)
    id = iud(qry,values)


    return '''<script>alert("Deleted successfully");window.location="/manage_products"</script>'''

@app.route('/viewmore')
@login_required
def viewmore():
    id=request.args['id']
    session['biilid']=id
    qry="SELECT `product`.*,`product_bill`.* FROM `product_bill` JOIN `booking` ON `booking`.`billid`=`product_bill`.`billid` JOIN `product` ON `product`.`Product_id`=`booking`.`pid` WHERE `booking`.`billid`=%s"
    val=(id)
    res=selectall(qry,val)
    print(res)
    return render_template("company/view product booking.html",val=res)


@app.route('/viewmore1')
@login_required
def viewmore1():
    id=request.args['id']
    session['biilid']=id
    qry="SELECT `product`.*,`product_bill`.* FROM `product_bill` JOIN `booking` ON `booking`.`billid`=`product_bill`.`billid` JOIN `product` ON `product`.`Product_id`=`booking`.`pid` WHERE `booking`.`billid`=%s"
    val=(id)
    res=selectall(qry,val)
    print(res)
    return render_template("company/view product report.html",val=res)


@app.route('/acceptbooking',methods=['post'])
@login_required
def acceptbooking():
    qry="update  product_bill set status='accept' where billid=%s"
    val=(str(session['biilid']))
    iud(qry,val)


    return '''<script>alert ("product booking accepted");window.location="/confirm_booking1"</script>'''




@app.route('/add_product1',methods=['post'])
@login_required
def add_product1():
    Name=request.form['textfield']
    Discription=request.form['textarea']
    Amount=request.form['textfield2']
    image = request.files['file']
    file = secure_filename(image.filename)
    image.save(os.path.join("static/files/",file))
    qry="insert into product values (null,%s,%s,%s,%s)"
    values=(Name,Discription,file,Amount)
    iud(qry,values)
    return '''<script>alert ("Product added");window.location="/manage_products"</script>'''


@app.route('/add_product',methods=['post'])
@login_required
def add_product():

    return render_template("company/Add product.html")

app.run(debug=True)