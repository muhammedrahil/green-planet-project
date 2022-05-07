/*
SQLyog Community v13.0.1 (64 bit)
MySQL - 5.5.20-log : Database - greenplanet
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`greenplanet` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `greenplanet`;

/*Table structure for table `assign_area` */

DROP TABLE IF EXISTS `assign_area`;

CREATE TABLE `assign_area` (
  `Area_id` int(11) NOT NULL AUTO_INCREMENT,
  `Vehicle_id` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Latitude` varchar(300) NOT NULL,
  `Longitude` varchar(300) NOT NULL,
  `Location` varchar(300) NOT NULL,
  PRIMARY KEY (`Area_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `assign_area` */

insert  into `assign_area`(`Area_id`,`Vehicle_id`,`Date`,`Latitude`,`Longitude`,`Location`) values 
(1,5,'2022-03-25','11.34','75.44','calicut');

/*Table structure for table `bill` */

DROP TABLE IF EXISTS `bill`;

CREATE TABLE `bill` (
  `Bill_id` int(11) NOT NULL AUTO_INCREMENT,
  `Request_id` int(20) NOT NULL,
  `Amount` bigint(20) NOT NULL,
  `Date` date NOT NULL,
  `Quantity` bigint(11) NOT NULL,
  `Waste_type` varchar(300) NOT NULL,
  PRIMARY KEY (`Bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `bill` */

insert  into `bill`(`Bill_id`,`Request_id`,`Amount`,`Date`,`Quantity`,`Waste_type`) values 
(1,1,56,'2022-03-25',0,'non recycle waste'),
(2,2,44,'2022-03-25',0,'recycle waste');

/*Table structure for table `booking` */

DROP TABLE IF EXISTS `booking`;

CREATE TABLE `booking` (
  `Booking_id` int(11) NOT NULL AUTO_INCREMENT,
  `billid` int(20) NOT NULL,
  `pid` int(11) NOT NULL,
  `Customer_id` int(11) NOT NULL,
  `amount` varchar(30) NOT NULL,
  `date` varbinary(44) NOT NULL,
  PRIMARY KEY (`Booking_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `booking` */

insert  into `booking`(`Booking_id`,`billid`,`pid`,`Customer_id`,`amount`,`date`) values 
(1,1,1,6,'123','2022-03-25');

/*Table structure for table `company_registration` */

DROP TABLE IF EXISTS `company_registration`;

CREATE TABLE `company_registration` (
  `companyid` int(20) NOT NULL AUTO_INCREMENT,
  `companyloginid` int(20) DEFAULT NULL,
  `companyname` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `post` varchar(50) DEFAULT NULL,
  `pin` int(20) DEFAULT NULL,
  `phone` bigint(50) DEFAULT NULL,
  `email` varchar(52) DEFAULT NULL,
  PRIMARY KEY (`companyid`),
  UNIQUE KEY `phone` (`phone`,`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `company_registration` */

insert  into `company_registration`(`companyid`,`companyloginid`,`companyname`,`place`,`post`,`pin`,`phone`,`email`) values 
(1,3,'company','calicut','calicut',123123,8976543456,'anu@gmail.com');

/*Table structure for table `complaint` */

DROP TABLE IF EXISTS `complaint`;

CREATE TABLE `complaint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `complaints` varchar(500) NOT NULL,
  `userid` int(11) NOT NULL,
  `date` date NOT NULL,
  `reply` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `complaint` */

insert  into `complaint`(`id`,`complaints`,`userid`,`date`,`reply`) values 
(1,'cmp',6,'2022-03-25','ll');

/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `Customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `Fname` varchar(300) NOT NULL,
  `Lname` varchar(300) NOT NULL,
  `Gender` varchar(300) NOT NULL,
  `Phone` bigint(20) NOT NULL,
  `Place` varchar(300) NOT NULL,
  `Post` varchar(300) NOT NULL,
  `Pin` bigint(20) NOT NULL,
  `E_mail` varchar(300) NOT NULL,
  `login_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `customer` */

insert  into `customer`(`Customer_id`,`Fname`,`Lname`,`Gender`,`Phone`,`Place`,`Post`,`Pin`,`E_mail`,`login_id`) values 
(1,'ammu','p','Female',6958362569,'place','post',695836,'anu@gmail.com',6);

/*Table structure for table `feedback` */

DROP TABLE IF EXISTS `feedback`;

CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `feedback` varchar(444) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `feedback` */

insert  into `feedback`(`id`,`date`,`uid`,`feedback`) values 
(1,'2022-03-25',6,'fd');

/*Table structure for table `kudubasree_work` */

DROP TABLE IF EXISTS `kudubasree_work`;

CREATE TABLE `kudubasree_work` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `work` varchar(43) DEFAULT NULL,
  `description` varchar(43) DEFAULT NULL,
  `k_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `kudubasree_work` */

insert  into `kudubasree_work`(`id`,`work`,`description`,`k_id`) values 
(2,'work','description',4);

/*Table structure for table `kudubasreereg` */

DROP TABLE IF EXISTS `kudubasreereg`;

CREATE TABLE `kudubasreereg` (
  `KUDUBASREEID` int(50) NOT NULL AUTO_INCREMENT,
  `KUBBSREELOGINID` int(50) DEFAULT NULL,
  `NANE` varchar(50) DEFAULT NULL,
  `PLACE` varchar(50) DEFAULT NULL,
  `POST` varchar(52) DEFAULT NULL,
  `PIN` int(47) DEFAULT NULL,
  `PHONE` bigint(50) DEFAULT NULL,
  PRIMARY KEY (`KUDUBASREEID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `kudubasreereg` */

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `Location_id` int(11) NOT NULL AUTO_INCREMENT,
  `Vehicle_id` int(11) NOT NULL,
  `Latitude` varchar(500) NOT NULL,
  `Longitude` varchar(500) NOT NULL,
  PRIMARY KEY (`Location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `location` */

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `Login_id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(300) NOT NULL,
  `Password` varchar(300) NOT NULL,
  `User_type` varchar(300) NOT NULL,
  PRIMARY KEY (`Login_id`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`Login_id`,`Username`,`Password`,`User_type`) values 
(3,'company1','123','company'),
(4,'jwala','123','KUDUBASREE'),
(5,'vehicle','123','vehicle'),
(6,'ammu','123','customer');

/*Table structure for table `notification` */

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `Notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `Notification` varchar(300) NOT NULL,
  `Date` date NOT NULL,
  PRIMARY KEY (`Notification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `notification` */

insert  into `notification`(`Notification_id`,`Notification`,`Date`) values 
(1,'noti','2022-03-25'),
(3,'ew','2022-03-26');

/*Table structure for table `pricechart` */

DROP TABLE IF EXISTS `pricechart`;

CREATE TABLE `pricechart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `chart` varchar(50) DEFAULT NULL,
  `companyid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `pricechart` */

insert  into `pricechart`(`id`,`type`,`chart`,`companyid`) values 
(2,'recycle','2021-08-10_2.png',3),
(3,'non recycle','2021-09-10.png',3);

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `Product_id` int(11) NOT NULL AUTO_INCREMENT,
  `Product_name` varchar(200) NOT NULL,
  `Details` varchar(200) NOT NULL,
  `Image` varchar(200) NOT NULL,
  `Price` varchar(500) NOT NULL,
  PRIMARY KEY (`Product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `product` */

insert  into `product`(`Product_id`,`Product_name`,`Details`,`Image`,`Price`) values 
(1,'pdt1','des','2021-09-27_1.png','123');

/*Table structure for table `product_bill` */

DROP TABLE IF EXISTS `product_bill`;

CREATE TABLE `product_bill` (
  `billid` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `date` varchar(45) DEFAULT NULL,
  `amount` varchar(45) DEFAULT NULL,
  `status` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`billid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `product_bill` */

insert  into `product_bill`(`billid`,`userid`,`date`,`amount`,`status`) values 
(1,6,'2022-03-25','123.0','accept');

/*Table structure for table `request_to_kudumbasree` */

DROP TABLE IF EXISTS `request_to_kudumbasree`;

CREATE TABLE `request_to_kudumbasree` (
  `reqid` int(11) NOT NULL AUTO_INCREMENT,
  `ulid` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `workid` int(11) DEFAULT NULL,
  `status` varchar(54) DEFAULT NULL,
  PRIMARY KEY (`reqid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `request_to_kudumbasree` */

insert  into `request_to_kudumbasree`(`reqid`,`ulid`,`date`,`workid`,`status`) values 
(1,6,'2022-03-25',2,'accept');

/*Table structure for table `training_video` */

DROP TABLE IF EXISTS `training_video`;

CREATE TABLE `training_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(50) DEFAULT NULL,
  `file` varchar(50) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `training_video` */

insert  into `training_video`(`id`,`description`,`file`,`status`) values 
(2,'video','jj.mp4','training'),
(4,'pgm','download_1.jfif','AWARNESS'),
(5,'kpo[','2021-08-07_1.png','AWARNESS');

/*Table structure for table `user_request` */

DROP TABLE IF EXISTS `user_request`;

CREATE TABLE `user_request` (
  `Request_id` int(11) NOT NULL AUTO_INCREMENT,
  `Customer_id` int(11) NOT NULL,
  `Assignarea_id` int(11) NOT NULL,
  `waste_type` varchar(45) DEFAULT NULL,
  `quantity` varchar(45) DEFAULT NULL,
  `Date` varchar(25) NOT NULL,
  `Status` varchar(300) NOT NULL,
  `lati` varchar(54) DEFAULT NULL,
  `longi` varchar(54) DEFAULT NULL,
  PRIMARY KEY (`Request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `user_request` */

insert  into `user_request`(`Request_id`,`Customer_id`,`Assignarea_id`,`waste_type`,`quantity`,`Date`,`Status`,`lati`,`longi`) values 
(1,6,1,'non recycle waste','4','2022-03-25','collected','',''),
(2,6,1,'recycle waste','2','2022-03-25','collected and cash paid','11.1162062','75.8415934');

/*Table structure for table `vehicle` */

DROP TABLE IF EXISTS `vehicle`;

CREATE TABLE `vehicle` (
  `Vehicle_id` int(11) NOT NULL AUTO_INCREMENT,
  `Model` varchar(300) NOT NULL,
  `Vehicle_no` varchar(300) NOT NULL,
  `Phone` bigint(20) NOT NULL,
  `Login_id` int(11) NOT NULL,
  `cmpyid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Vehicle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `vehicle` */

insert  into `vehicle`(`Vehicle_id`,`Model`,`Vehicle_no`,`Phone`,`Login_id`,`cmpyid`) values 
(1,'MIKML','566',8976543456,5,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
