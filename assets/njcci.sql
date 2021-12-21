/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : njcci

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 08/06/2021 15:27:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_info
-- ----------------------------
DROP TABLE IF EXISTS `address_info`;
CREATE TABLE `address_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `addressee_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `addressee_telphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '//1.默认地址，2.已保存，3.已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address_info
-- ----------------------------
INSERT INTO `address_info` VALUES (1, 9, 'Young', '18075042107', '江苏省 南京市 栖霞区 文苑路 9号', '100000', 1);
INSERT INTO `address_info` VALUES (2, 9, 'Young', '18075042107', '111111', '100000', 2);
INSERT INTO `address_info` VALUES (3, 9, 'Young', '18075042107', '111111111111111111111111', '100000', 2);
INSERT INTO `address_info` VALUES (4, 10, '123', '18250397886', '123', '364100', 1);

-- ----------------------------
-- Table structure for cart_info
-- ----------------------------
DROP TABLE IF EXISTS `cart_info`;
CREATE TABLE `cart_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `item_id` int(11) NOT NULL DEFAULT 0,
  `amount` int(11) NOT NULL DEFAULT 1,
  `price` double(10, 2) NOT NULL DEFAULT 0.00,
  `total_price` double(10, 2) NOT NULL DEFAULT 0.00,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '//1.待下单，2.已删除，3.已失效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_info
-- ----------------------------
INSERT INTO `cart_info` VALUES (1, 1, 2, 3, 10.00, 30.00, '2020-02-11 05:03:31', '2020-02-11 05:51:56', 2);
INSERT INTO `cart_info` VALUES (2, 1, 2, 1, 10.00, 10.00, '2020-02-11 05:53:56', '2020-02-11 05:53:56', 2);
INSERT INTO `cart_info` VALUES (3, 1, 2, 1, 10.00, 10.00, '2020-02-11 06:33:38', '2020-02-11 06:33:38', 2);
INSERT INTO `cart_info` VALUES (4, 1, 2, 14, 10.00, 140.00, '2020-02-11 07:45:40', '2020-02-15 12:32:46', 2);
INSERT INTO `cart_info` VALUES (5, 1, 3, 1, 1559.50, 1559.50, '2020-02-14 12:27:07', '2020-02-15 15:19:04', 2);
INSERT INTO `cart_info` VALUES (6, 9, 3, 11, 1559.50, 17154.50, '2020-02-14 17:13:54', '2020-02-17 12:08:41', 2);
INSERT INTO `cart_info` VALUES (7, 1, 4, 1, 599.00, 599.00, '2020-02-15 15:17:59', '2020-02-15 15:19:06', 2);
INSERT INTO `cart_info` VALUES (8, 1, 3, 1, 1559.50, 1559.50, '2020-02-16 14:35:17', '2020-02-16 14:35:17', 2);
INSERT INTO `cart_info` VALUES (9, 1, 3, 1, 1559.50, 1559.50, '2020-02-16 14:39:27', '2020-02-16 14:39:27', 2);
INSERT INTO `cart_info` VALUES (10, 10, 4, 2, 599.00, 1198.00, '2020-02-20 01:59:10', '2020-02-20 01:59:16', 2);
INSERT INTO `cart_info` VALUES (11, 10, 13, 1, 2590.00, 2590.00, '2020-02-20 02:01:19', '2020-02-20 02:01:19', 1);
INSERT INTO `cart_info` VALUES (12, 10, 9, 2, 899.00, 1798.00, '2020-02-20 02:05:40', '2020-02-20 02:05:40', 1);
INSERT INTO `cart_info` VALUES (13, 10, 2, 1, 1830.00, 1830.00, '2020-02-20 02:13:18', '2020-02-20 02:13:18', 1);
INSERT INTO `cart_info` VALUES (14, 1, 5, 1, 2025.80, 2025.80, '2020-02-20 02:13:32', '2020-02-20 02:13:32', 1);

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '//种类：1水果，2水产，3肉类，4鸡蛋，5蔬菜，6速冻产品',
  `price` double(10, 2) NOT NULL DEFAULT 0.00,
  `sales` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NULL,
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `update_time` datetime(0) NULL,
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '//状态：1销售中，2已下架',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (2, '橱柜', '暂无', 1, 1830.00, 0, '2020-02-07 18:58:20', '芝华仕', '2020-02-11 07:23:43', 1, 'images/goods/goods003.jpg');
INSERT INTO `item` VALUES (3, '实木北欧床', '暂无', 1, 1559.50, 48, '2020-02-10 12:48:14', '芝华仕', '2020-02-10 12:48:14', 1, 'images/goods/goods002.jpg');
INSERT INTO `item` VALUES (4, '床垫', '暂无', 1, 599.00, 22, '2020-02-13 00:17:17', '芝华仕', '2020-02-13 00:17:28', 1, 'images/goods/goods001.jpg');
INSERT INTO `item` VALUES (5, '嵌入式木床', '暂无', 1, 2025.80, 6, '2020-02-13 00:18:15', '芝华仕', '2020-02-13 00:18:21', 1, 'images/goods/goods004.jpg');
INSERT INTO `item` VALUES (6, '原木餐桌', '暂无', 2, 2548.00, 0, '2020-02-18 15:18:29', '芝华仕', '2020-02-18 15:18:42', 1, 'images/goods/goods005.jpg');
INSERT INTO `item` VALUES (7, '精美全套餐具', '暂无', 2, 206.00, 0, '2020-02-18 15:19:31', '芝华仕', '2020-02-18 15:19:39', 1, 'images/goods/goods006.jpg');
INSERT INTO `item` VALUES (8, '胡桃木餐桌', '暂无', 2, 2039.00, 0, '2020-02-18 15:20:14', '芝华仕', '2020-02-18 15:20:24', 1, 'images/goods/goods007.jpg');
INSERT INTO `item` VALUES (9, '餐厅吊灯', '暂无', 2, 899.00, 0, '2020-02-18 15:21:03', '芝华仕', '2020-02-18 15:21:09', 1, 'images/goods/goods008.jpg');
INSERT INTO `item` VALUES (10, '组合布艺沙发', '暂无', 3, 3799.00, 0, '2020-02-18 15:22:24', '芝华仕', '2020-02-18 15:22:29', 1, 'images/goods/goods009.jpg');
INSERT INTO `item` VALUES (11, '独个深灰沙发', '暂无', 3, 608.00, 0, '2020-02-18 15:23:18', '芝华仕', '2020-02-18 15:23:26', 1, 'images/goods/goods010.jpg');
INSERT INTO `item` VALUES (12, '简约长沙发', '暂无', 3, 1538.00, 0, '2020-02-18 15:24:01', '芝华仕', '2020-02-18 15:24:06', 1, 'images/goods/goods011.jpg');
INSERT INTO `item` VALUES (13, '电视柜茶几组合', '暂无', 3, 2590.00, 0, '2020-02-18 15:24:44', '芝华仕', '2020-02-18 15:24:50', 1, 'images/goods/goods012.jpg');
INSERT INTO `item` VALUES (14, '美丽家具', '暂无', 4, 998.00, 0, '2020-02-20 02:33:20', '芝华仕', '2020-02-20 02:33:20', 1, 'images/goods/goods012.jpg');

-- ----------------------------
-- Table structure for item_stock
-- ----------------------------
DROP TABLE IF EXISTS `item_stock`;
CREATE TABLE `item_stock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` int(11) NOT NULL DEFAULT 0,
  `stock` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_stock
-- ----------------------------
INSERT INTO `item_stock` VALUES (2, 2, 1000);
INSERT INTO `item_stock` VALUES (3, 3, 952);
INSERT INTO `item_stock` VALUES (4, 4, 978);
INSERT INTO `item_stock` VALUES (5, 5, 994);
INSERT INTO `item_stock` VALUES (6, 6, 1000);
INSERT INTO `item_stock` VALUES (7, 7, 1000);
INSERT INTO `item_stock` VALUES (8, 8, 1000);
INSERT INTO `item_stock` VALUES (9, 9, 1000);
INSERT INTO `item_stock` VALUES (10, 10, 1000);
INSERT INTO `item_stock` VALUES (11, 11, 1000);
INSERT INTO `item_stock` VALUES (12, 12, 1000);
INSERT INTO `item_stock` VALUES (13, 13, 1000);
INSERT INTO `item_stock` VALUES (14, 14, 100);

-- ----------------------------
-- Table structure for logistics_info
-- ----------------------------
DROP TABLE IF EXISTS `logistics_info`;
CREATE TABLE `logistics_info`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address_id` int(11) NOT NULL DEFAULT 0,
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NULL,
  `delivery_company` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `delivery_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '//1.代发货，2.已发货',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of logistics_info
-- ----------------------------
INSERT INTO `logistics_info` VALUES ('20200216000005', 1, '20200216000050', '2020-02-16 14:39:49', '顺丰', '10000000000000', 2, '芝华仕');
INSERT INTO `logistics_info` VALUES ('20200217000006', 1, '20200217000065', '2020-02-17 12:51:33', '', '', 1, '芝华仕');
INSERT INTO `logistics_info` VALUES ('20200217000007', 1, '20200217000066', '2020-02-17 13:16:41', '', '', 1, '芝华仕');
INSERT INTO `logistics_info` VALUES ('20200217000008', 1, '20200217000062', '2020-02-17 13:24:44', '', '', 1, '芝华仕');
INSERT INTO `logistics_info` VALUES ('20200217000009', 1, '20200217000061', '2020-02-17 13:25:03', '', '', 1, '芝华仕');
INSERT INTO `logistics_info` VALUES ('20200220000010', 4, '20200220000067', '2020-02-20 02:11:48', '顺丰快递', '578235317371', 2, '芝华仕');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT 0,
  `item_price` double(10, 2) NOT NULL DEFAULT 0.00,
  `amount` int(11) NOT NULL DEFAULT 0,
  `order_price` double(10, 2) NOT NULL DEFAULT 0.00,
  `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '//1.待支付，2.已付款，3.已取消',
  `item_id` int(11) NOT NULL,
  `payment_method` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('20200207000002', 0, 10.00, 1, 10.00, '2020-02-07 22:48:12', 2, 2, 1);
INSERT INTO `order_info` VALUES ('20200209000003', 0, 10.00, 5, 50.00, '2020-02-09 09:29:30', 3, 2, 0);
INSERT INTO `order_info` VALUES ('20200209000004', 0, 10.00, 5, 50.00, '2020-02-09 09:32:15', 3, 2, 0);
INSERT INTO `order_info` VALUES ('20200214000005', 1, 1559.50, 1, 1559.50, '2020-02-14 12:28:10', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200214000006', 1, 1559.50, 1, 1559.50, '2020-02-14 12:29:04', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200214000007', 1, 1559.50, 1, 1559.50, '2020-02-14 12:29:16', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200215000008', 1, 2025.80, 1, 2025.80, '2020-02-15 14:17:11', 1, 5, 0);
INSERT INTO `order_info` VALUES ('20200216000009', 1, 1559.50, 1, 1559.50, '2020-02-15 16:46:22', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000010', 1, 599.00, 1, 599.00, '2020-02-15 16:46:22', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000011', 1, 1559.50, 1, 1559.50, '2020-02-15 16:50:19', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000012', 1, 599.00, 1, 599.00, '2020-02-15 16:50:19', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000013', 1, 1559.50, 1, 1559.50, '2020-02-15 16:50:33', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000014', 1, 599.00, 1, 599.00, '2020-02-15 16:50:33', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000015', 1, 1559.50, 1, 1559.50, '2020-02-15 16:50:35', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000016', 1, 599.00, 1, 599.00, '2020-02-15 16:50:35', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000017', 1, 1559.50, 1, 1559.50, '2020-02-15 16:56:17', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000018', 1, 599.00, 1, 599.00, '2020-02-15 16:56:17', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000019', 1, 1559.50, 1, 1559.50, '2020-02-15 16:58:35', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000020', 1, 599.00, 1, 599.00, '2020-02-15 16:58:35', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000021', 1, 1559.50, 1, 1559.50, '2020-02-15 17:00:32', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000022', 1, 599.00, 1, 599.00, '2020-02-15 17:00:32', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000023', 1, 1559.50, 1, 1559.50, '2020-02-15 17:04:19', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000024', 1, 599.00, 1, 599.00, '2020-02-15 17:04:19', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000025', 1, 1559.50, 1, 1559.50, '2020-02-16 03:35:05', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200216000026', 1, 599.00, 1, 599.00, '2020-02-16 03:35:05', 2, 4, 1);
INSERT INTO `order_info` VALUES ('20200216000027', 1, 1559.50, 1, 1559.50, '2020-02-16 03:38:48', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000028', 1, 599.00, 1, 599.00, '2020-02-16 03:38:48', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000029', 1, 1559.50, 1, 1559.50, '2020-02-16 03:38:55', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000030', 1, 599.00, 1, 599.00, '2020-02-16 03:38:55', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000031', 1, 1559.50, 1, 1559.50, '2020-02-16 03:44:59', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000032', 1, 599.00, 1, 599.00, '2020-02-16 03:44:59', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000033', 1, 1559.50, 1, 1559.50, '2020-02-16 03:45:47', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000034', 1, 599.00, 1, 599.00, '2020-02-16 03:45:47', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000035', 1, 1559.50, 1, 1559.50, '2020-02-16 03:46:08', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200216000036', 1, 599.00, 1, 599.00, '2020-02-16 03:46:08', 2, 4, 1);
INSERT INTO `order_info` VALUES ('20200216000037', 1, 1559.50, 1, 1559.50, '2020-02-16 05:11:13', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000038', 1, 599.00, 1, 599.00, '2020-02-16 05:11:14', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000039', 1, 1559.50, 1, 1559.50, '2020-02-16 05:11:26', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000040', 1, 599.00, 1, 599.00, '2020-02-16 05:11:26', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000041', 1, 1559.50, 1, 1559.50, '2020-02-16 05:33:50', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000042', 1, 599.00, 1, 599.00, '2020-02-16 05:33:50', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000043', 1, 1559.50, 1, 1559.50, '2020-02-16 05:36:21', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000044', 1, 599.00, 1, 599.00, '2020-02-16 05:36:21', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000045', 1, 1559.50, 1, 1559.50, '2020-02-17 21:10:55', 3, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000046', 1, 599.00, 1, 599.00, '2020-02-16 05:37:38', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000047', 1, 1559.50, 1, 1559.50, '2020-02-16 05:50:13', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000048', 1, 599.00, 1, 599.00, '2020-02-16 05:50:13', 1, 4, 0);
INSERT INTO `order_info` VALUES ('20200216000049', 1, 1559.50, 1, 1559.50, '2020-02-16 14:35:25', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000050', 1, 1559.50, 1, 1559.50, '2020-02-16 14:39:32', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200216000051', 1, 1559.50, 1, 1559.50, '2020-02-16 15:03:54', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000052', 1, 1559.50, 1, 1559.50, '2020-02-16 15:04:54', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200216000053', 1, 1559.50, 1, 1559.50, '2020-02-16 15:05:22', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000054', 9, 1559.50, 11, 17154.50, '2020-02-17 12:09:24', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000055', 1, 1559.50, 1, 1559.50, '2020-02-17 12:39:05', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000056', 1, 1559.50, 1, 1559.50, '2020-02-17 12:40:31', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000057', 1, 1559.50, 1, 1559.50, '2020-02-17 12:41:47', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000058', 1, 1559.50, 1, 1559.50, '2020-02-17 12:41:50', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000059', 1, 1559.50, 1, 1559.50, '2020-02-17 12:44:00', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000060', 1, 1559.50, 1, 1559.50, '2020-02-17 12:45:04', 1, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000061', 1, 1559.50, 1, 1559.50, '2020-02-17 12:45:06', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200217000062', 1, 1559.50, 1, 1559.50, '2020-02-17 12:46:15', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200217000063', 1, 1559.50, 1, 1559.50, '2020-02-17 21:15:09', 3, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000064', 1, 1559.50, 1, 1559.50, '2020-02-17 21:15:01', 3, 3, 0);
INSERT INTO `order_info` VALUES ('20200217000065', 1, 1559.50, 1, 1559.50, '2020-02-17 12:51:31', 2, 3, 1);
INSERT INTO `order_info` VALUES ('20200217000066', 1, 2025.80, 5, 10129.00, '2020-02-17 13:16:38', 2, 5, 1);
INSERT INTO `order_info` VALUES ('20200220000067', 10, 599.00, 2, 1198.00, '2020-02-20 01:59:36', 2, 4, 1);
INSERT INTO `order_info` VALUES ('20200220000068', 10, 899.00, 3, 2697.00, '2020-02-20 10:11:11', 3, 9, 0);

-- ----------------------------
-- Table structure for sequence_info
-- ----------------------------
DROP TABLE IF EXISTS `sequence_info`;
CREATE TABLE `sequence_info`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `current_value` int(11) NOT NULL DEFAULT 0,
  `step` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sequence_info
-- ----------------------------
INSERT INTO `sequence_info` VALUES ('logistics_info', 11, 1);
INSERT INTO `sequence_info` VALUES ('order_info', 69, 1);

-- ----------------------------
-- Table structure for store_info
-- ----------------------------
DROP TABLE IF EXISTS `store_info`;
CREATE TABLE `store_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `telphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_info
-- ----------------------------
INSERT INTO `store_info` VALUES (2, '自营', '中国-安徽-六安', '12345678911', '2020-02-10 12:42:44', '2020-02-10 12:42:44');
INSERT INTO `store_info` VALUES (3, '芝华仕', '南京邮电大学仙林校区', '18075042107', '2020-02-16 07:57:57', '2020-02-16 07:57:57');
INSERT INTO `store_info` VALUES (5, '123', '1111111', '13635645558', '2021-05-27 06:27:57', '2021-05-27 06:27:57');

-- ----------------------------
-- Table structure for store_password
-- ----------------------------
DROP TABLE IF EXISTS `store_password`;
CREATE TABLE `store_password`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_id` int(11) NOT NULL DEFAULT 0,
  `encrpt_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_password
-- ----------------------------
INSERT INTO `store_password` VALUES (1, 2, 'yIN7I/+Kqoot3pFUc84JkQ==', '2020-02-10 12:42:44', '2020-02-10 12:42:44');
INSERT INTO `store_password` VALUES (2, 3, '3703e104432e85c8ef06d5defe0e053a', '2020-02-16 07:57:57', '2020-02-16 07:57:57');
INSERT INTO `store_password` VALUES (4, 5, '3703e104432e85c8ef06d5defe0e053a', '2021-05-27 06:27:57', '2021-05-27 06:27:57');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `telphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `gender` int(11) NOT NULL DEFAULT 0 COMMENT '//1.男，2.女',
  `age` int(11) NOT NULL DEFAULT 0,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'young1', '12345678910', '123456@abc.com', 1, 18, '2020-02-09 15:14:33', '2020-02-16 12:56:56');
INSERT INTO `user_info` VALUES (9, '哈哈哈', '18075042107', '568462483@qq.com', 1, 20, '2020-02-14 17:00:21', '2020-02-14 17:00:21');
INSERT INTO `user_info` VALUES (10, '大宝贝', '18250397886', '2429628776@qq.com', 2, 20, '2020-02-20 01:54:45', '2020-02-20 01:57:27');
INSERT INTO `user_info` VALUES (11, '杨阳yang', '18075042108', 'y1030725545@gmail.com', 1, 18, '2021-03-26 15:07:08', '2021-03-26 15:07:08');
INSERT INTO `user_info` VALUES (13, 'young', '13635645558', '12345666@abc.com', 1, 18, '2021-05-27 04:00:32', '2021-05-27 04:00:32');

-- ----------------------------
-- Table structure for user_password
-- ----------------------------
DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `encrpt_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_password
-- ----------------------------
INSERT INTO `user_password` VALUES (1, 1, 'yIN7I/+Kqoot3pFUc84JkQ==', '2020-02-09 15:14:33', '2020-02-09 15:14:33');
INSERT INTO `user_password` VALUES (7, 9, '3703e104432e85c8ef06d5defe0e053a', '2020-02-14 17:00:21', '2020-02-14 17:00:21');
INSERT INTO `user_password` VALUES (8, 10, 'JdVa0oOqQAr0ZMdtcTwHrQ==', '2020-02-20 01:54:45', '2020-02-20 01:54:45');
INSERT INTO `user_password` VALUES (9, 11, 'AKHxh3IcY1ATVr95Hmk4LA==', '2021-03-26 15:07:08', '2021-03-26 15:07:08');
INSERT INTO `user_password` VALUES (11, 13, '3703e104432e85c8ef06d5defe0e053a', '2021-05-27 04:00:32', '2021-05-27 04:00:32');

SET FOREIGN_KEY_CHECKS = 1;
