/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云12月到期
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 43.143.200.231:3306
 Source Schema         : boot3_security

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 10/11/2023 17:50:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for acl_menu
-- ----------------------------
DROP TABLE IF EXISTS `acl_menu`;
CREATE TABLE `acl_menu`  (
  `acme_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `acme_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `acme_pid` bigint(20) NULL DEFAULT NULL COMMENT '父级，0为顶级',
  `acme_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
  `acme_type` int(11) NULL DEFAULT NULL COMMENT '类型 1菜单 2按钮',
  `acme_sort` int(11) NULL DEFAULT 1 COMMENT '排序',
  `acme_select` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否可选Y是N否',
  `acme_status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级',
  PRIMARY KEY (`acme_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_menu
-- ----------------------------
INSERT INTO `acl_menu` VALUES (1, '全部数据', 0, NULL, NULL, 1, NULL, NULL, '2023-06-24 10:12:42', '2023-06-26 14:39:21', 1);
INSERT INTO `acl_menu` VALUES (2, '系统管理', 1, 'System', 1, 10, NULL, NULL, '2023-06-26 15:04:14', '2023-06-26 15:11:34', 2);
INSERT INTO `acl_menu` VALUES (3, '用户管理', 2, 'AclUser', 1, 1, NULL, NULL, '2023-06-26 15:04:28', '2023-06-26 15:12:08', 3);
INSERT INTO `acl_menu` VALUES (4, '角色管理', 2, 'AclRole', 1, 2, NULL, NULL, '2023-06-26 15:04:44', '2023-06-26 15:12:10', 3);
INSERT INTO `acl_menu` VALUES (5, '接口管理', 2, 'AclInterface', 1, 3, NULL, NULL, '2023-06-26 15:05:01', '2023-06-26 15:12:12', 3);
INSERT INTO `acl_menu` VALUES (6, '菜单管理', 2, 'AclMenu', 1, 4, NULL, NULL, '2023-06-26 15:05:18', '2023-06-26 15:12:13', 3);
INSERT INTO `acl_menu` VALUES (7, '添加用户', 3, 'AclUser.add', 2, 1, '', NULL, '2023-06-26 15:08:52', '2023-06-26 15:12:16', 4);
INSERT INTO `acl_menu` VALUES (19, '删除用户', 3, 'AclUser.delete', 2, 2, NULL, NULL, '2023-06-26 17:48:21', '2023-06-26 17:48:21', 4);
INSERT INTO `acl_menu` VALUES (20, '批量删除', 3, 'AclUser.mutipleDelete', 2, 3, NULL, NULL, '2023-07-25 15:40:58', '2023-07-25 15:40:58', 4);
INSERT INTO `acl_menu` VALUES (21, '日志管理', 2, 'Logs', 1, 6, NULL, NULL, '2023-11-02 03:42:40', '2023-11-02 03:42:40', 3);

-- ----------------------------
-- Table structure for acl_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `acl_menu_role`;
CREATE TABLE `acl_menu_role`  (
  `acmr_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `acmr_menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单id',
  `acmr_role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `acmr_role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `acmr_role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色代码',
  PRIMARY KEY (`acmr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_menu_role
-- ----------------------------
INSERT INTO `acl_menu_role` VALUES (131, 7, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (132, 3, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (133, 2, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (134, 1, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (135, 4, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (136, 5, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (137, 6, 2, '普通用户', 'normalUser');
INSERT INTO `acl_menu_role` VALUES (138, 1, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (139, 2, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (140, 3, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (141, 7, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (142, 19, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (143, 20, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (144, 4, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (145, 5, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (146, 6, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_menu_role` VALUES (147, 21, 1, '超级管理员', 'superAdmin');

-- ----------------------------
-- Table structure for acl_path
-- ----------------------------
DROP TABLE IF EXISTS `acl_path`;
CREATE TABLE `acl_path`  (
  `acpa_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `acpa_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径名',
  `acpa_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `acpa_pid` bigint(20) NULL DEFAULT NULL COMMENT '上级id',
  `acpa_type` int(11) NULL DEFAULT 1 COMMENT '类型 0分类 1路径',
  `acpa_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `acpa_sort` int(11) NULL DEFAULT 1 COMMENT '排序',
  `is_deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`acpa_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_path
-- ----------------------------
INSERT INTO `acl_path` VALUES (1, '根目录', NULL, -1, 0, '根', 1, 0, '2023-05-30 10:32:42', '2023-05-30 10:32:44');
INSERT INTO `acl_path` VALUES (2, '系统管理', NULL, 1, 0, NULL, 1, 0, '2023-07-27 11:16:13', '2023-07-27 11:16:15');
INSERT INTO `acl_path` VALUES (3, '用户管理', '', 2, 0, NULL, 1, 0, '2023-07-27 11:16:16', '2023-07-27 11:16:18');
INSERT INTO `acl_path` VALUES (5, '删除用户', '/system/acl-user/delete', 3, 1, NULL, 2, 0, '2023-07-27 11:16:22', '2023-07-27 11:16:25');
INSERT INTO `acl_path` VALUES (6, '添加用户', '/system/acl-user/insert', 3, 1, NULL, 3, 0, '2023-07-01 22:17:43', '2023-07-01 22:17:43');
INSERT INTO `acl_path` VALUES (8, '菜单管理', '', 2, 0, NULL, 5, 0, '2023-07-01 22:26:58', '2023-07-01 22:26:58');
INSERT INTO `acl_path` VALUES (11, '添加菜单', '/system/acl-menu/insert', 8, 1, NULL, 2, 0, '2023-07-01 22:32:16', '2023-07-01 22:32:16');
INSERT INTO `acl_path` VALUES (12, '更新菜单', '/system/acl-menu/update', 8, 1, NULL, 3, 0, '2023-07-01 22:32:31', '2023-07-01 22:32:31');
INSERT INTO `acl_path` VALUES (13, '删除菜单', '/system/acl-menu/delete', 8, 1, NULL, 4, 0, '2023-07-01 22:32:50', '2023-07-01 22:32:50');
INSERT INTO `acl_path` VALUES (14, '批量删除用户', '/system/acl-user/deleteBatch', 3, 1, NULL, 4, 0, '2023-07-25 15:47:11', '2023-07-25 15:47:11');
INSERT INTO `acl_path` VALUES (15, '角色管理', '', 2, 0, NULL, 3, 0, '2023-09-18 02:04:35', '2023-09-18 02:04:35');
INSERT INTO `acl_path` VALUES (16, '插入角色', '/system/acl-role/insert', 15, 1, NULL, 1, 0, '2023-09-18 02:06:23', '2023-09-18 02:06:23');
INSERT INTO `acl_path` VALUES (17, '更新角色', '/system/acl-role/update', 15, 1, NULL, 2, 0, '2023-09-18 02:06:44', '2023-09-18 02:06:44');
INSERT INTO `acl_path` VALUES (18, '删除角色', '/system/acl-role/delete', 15, 1, NULL, 3, 0, '2023-09-18 02:06:59', '2023-09-18 02:06:59');
INSERT INTO `acl_path` VALUES (19, '接口管理', '', 2, 0, NULL, 4, 0, '2023-09-18 02:09:12', '2023-09-18 02:09:12');
INSERT INTO `acl_path` VALUES (20, '插入接口', '/system/acl-path/insert', 19, 1, NULL, 1, 0, '2023-09-18 02:10:29', '2023-09-18 02:10:29');
INSERT INTO `acl_path` VALUES (21, '更新接口', '/system/acl-path/update', 19, 1, NULL, 2, 0, '2023-09-18 02:10:46', '2023-09-18 02:10:46');
INSERT INTO `acl_path` VALUES (22, '删除接口', '/system/acl-path/delete', 19, 1, NULL, 3, 0, '2023-09-18 02:10:59', '2023-09-18 02:10:59');
INSERT INTO `acl_path` VALUES (23, '配置接口', '/system/acl-path-role/updatePathByRole', 19, 1, NULL, 5, 0, '2023-09-18 02:11:44', '2023-09-18 02:11:44');
INSERT INTO `acl_path` VALUES (24, '配置菜单', '/system/acl-menu-role/update', 8, 1, NULL, 4, 0, '2023-09-18 09:18:06', '2023-09-18 09:18:06');
INSERT INTO `acl_path` VALUES (25, '日志管理', '', 2, 0, NULL, 6, 0, '2023-10-25 03:06:33', '2023-10-25 03:06:33');
INSERT INTO `acl_path` VALUES (26, '查询日志', '/system/sys-history-logs/selectByExample', 25, 1, NULL, 1, 0, '2023-10-25 03:07:04', '2023-10-25 03:07:04');

-- ----------------------------
-- Table structure for acl_path_role
-- ----------------------------
DROP TABLE IF EXISTS `acl_path_role`;
CREATE TABLE `acl_path_role`  (
  `acpr_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `acpr_path_id` bigint(20) NULL DEFAULT NULL COMMENT '路径id',
  `acpr_role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `acpr_role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `acpr_role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色代码',
  PRIMARY KEY (`acpr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 82 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_path_role
-- ----------------------------
INSERT INTO `acl_path_role` VALUES (67, 5, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (68, 6, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (69, 14, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (70, 16, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (71, 17, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (72, 18, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (73, 20, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (74, 21, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (75, 22, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (76, 23, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (77, 11, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (78, 12, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (79, 24, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (80, 13, 1, '超级管理员', 'superAdmin');
INSERT INTO `acl_path_role` VALUES (81, 26, 1, '超级管理员', 'superAdmin');

-- ----------------------------
-- Table structure for acl_role
-- ----------------------------
DROP TABLE IF EXISTS `acl_role`;
CREATE TABLE `acl_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `role_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码',
  `role_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除 0否 1是',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_role
-- ----------------------------
INSERT INTO `acl_role` VALUES (1, '超级管理员', 'superAdmin', NULL, '2023-05-30 10:17:23', '2023-05-30 10:17:25', 0);
INSERT INTO `acl_role` VALUES (2, '普通用户', 'normalUser', '', '2023-06-12 20:28:13', '2023-06-12 20:28:15', 0);
INSERT INTO `acl_role` VALUES (3, '大萨达', '12313', NULL, '2023-06-12 20:49:01', '2023-06-12 20:50:18', 1);
INSERT INTO `acl_role` VALUES (4, '仓管', 'repository', NULL, '2023-06-12 21:16:23', '2023-07-02 17:42:28', 0);

-- ----------------------------
-- Table structure for acl_user
-- ----------------------------
DROP TABLE IF EXISTS `acl_user`;
CREATE TABLE `acl_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `user_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` int(11) NULL DEFAULT 0 COMMENT '性别 0无 1男 2女',
  `is_deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_user
-- ----------------------------
INSERT INTO `acl_user` VALUES (1, 'admin', 'e6c08ad5832df0845082f831d1d148f43882b7962dc75c4f', '管理员', '管理员', '18173128326', '6852052314487274', '/upload/2023-09-11/image/vac092815Zo49.jpg', 1, 0, '2023-06-13 19:44:33', '2023-11-10 09:49:38');
INSERT INTO `acl_user` VALUES (2, 'admin2', '08845824563b975f0ba7337c710284405a0645743ae0fb05', '夏平', '兵部尚书', '13626466174', '8543707718005300', '/upload/2023-09-11/image/dgq092824vFLQ.jpg', 1, 0, '2023-06-13 19:44:36', '2023-09-11 01:28:25');
INSERT INTO `acl_user` VALUES (3, 'admin3', '96248395732e90f15d72f33f012d4da4348c37eb27978d41', '护盾', '123', '123123123', '6852052314487274', '/upload/2023-09-11/image/1_deeeelete_1569373353092854W748.png', 2, 0, '2023-06-14 08:51:32', '2023-09-18 09:15:07');
INSERT INTO `acl_user` VALUES (4, 'kubernate', '329b71611a43b2dd02114d9978cc76283a12c9213c44f193', '江益冉', '杜添池', '19797861629', '2714201987819349', '/default.png', 2, 0, '2023-06-14 16:41:25', '2023-09-11 02:21:57');
INSERT INTO `acl_user` VALUES (5, 'id123kdsai', '68966f96459898524861120a060116217a0a25da22c6695e', '雷泽惠', 'rui', '19382112640', '8669841061105265', '/upload/2023-08-23/image/img05253387H9.jpg', 2, 0, '2023-06-14 16:42:13', '2023-08-23 09:25:34');
INSERT INTO `acl_user` VALUES (6, '伏地魔', '081d9676a11226395be37f40b93125f32d6ab5d258b4fa28', '马晶滢', '侯晶莹', '19559152213', '8961653492365542', '/upload/2023-06-14/image/Snipaste_2022-09-29_19-45-0104412452Kl.png', 2, 1, '2023-06-21 18:52:09', '2023-08-23 09:26:14');
INSERT INTO `acl_user` VALUES (7, '445123788', '08a94a54d15153b59d829c9e276b46e16352b4f587140643', '万艳', 'sad', '14997321010', '8445392974154844', '/upload/2023-08-23/image/logo052504rW33.png', 1, 0, '2023-06-21 18:52:46', '2023-08-23 09:25:06');

-- ----------------------------
-- Table structure for acl_user_role
-- ----------------------------
DROP TABLE IF EXISTS `acl_user_role`;
CREATE TABLE `acl_user_role`  (
  `acur_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `acur_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户Id',
  `acur_role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`acur_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色用户绑定' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of acl_user_role
-- ----------------------------
INSERT INTO `acl_user_role` VALUES (1, 1, 1, '2023-05-30 10:23:21', '2023-05-30 10:23:23');
INSERT INTO `acl_user_role` VALUES (2, 1, 2, '2023-07-27 11:15:23', '2023-07-27 11:15:26');
INSERT INTO `acl_user_role` VALUES (9, 3, 2, '2023-06-23 12:45:03', '2023-06-23 12:45:03');
INSERT INTO `acl_user_role` VALUES (10, 4, 2, '2023-06-23 12:45:07', '2023-06-23 12:45:07');
INSERT INTO `acl_user_role` VALUES (11, 5, 4, '2023-06-23 12:45:12', '2023-06-23 12:45:12');
INSERT INTO `acl_user_role` VALUES (13, 6, 2, '2023-06-23 12:45:23', '2023-06-23 12:45:23');
INSERT INTO `acl_user_role` VALUES (14, 6, 4, '2023-06-23 12:45:23', '2023-06-23 12:45:23');
INSERT INTO `acl_user_role` VALUES (15, 7, 4, '2023-06-23 12:58:45', '2023-06-23 12:58:45');
INSERT INTO `acl_user_role` VALUES (16, 2, 2, '2023-07-01 19:16:27', '2023-07-01 19:16:27');

-- ----------------------------
-- Table structure for sys_history_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_history_logs`;
CREATE TABLE `sys_history_logs`  (
  `slog_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `slog_login_user_id` bigint(20) NULL DEFAULT NULL COMMENT '登录用户id',
  `slog_request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求者ip',
  `slog_request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `slog_request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `slog_request_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求载荷',
  `slog_response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '响应数据',
  `slog_user_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `gmt_create` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `slog_client` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端源',
  PRIMARY KEY (`slog_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_history_logs
-- ----------------------------
INSERT INTO `sys_history_logs` VALUES (1, 1, '123.168.117.75', '新增接口', '/system/acl-path/insert', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-10-25 03:06:34', '2023-10-25 03:06:33', '后台管理端');
INSERT INTO `sys_history_logs` VALUES (2, 1, '123.168.117.75', '新增接口', '/system/acl-path/insert', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-10-25 03:07:05', '2023-10-25 03:07:04', '后台管理端');
INSERT INTO `sys_history_logs` VALUES (3, 1, '123.168.117.75', '根据角色更新对应的接口', '/system/acl-path-role/updatePathByRole', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-10-25 03:15:32', '2023-10-25 03:15:32', '后台管理端');
INSERT INTO `sys_history_logs` VALUES (4, 1, '123.168.117.75', '插入菜单', '/system/acl-menu/insert', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-11-02 03:42:41', '2023-11-02 03:42:40', '后台管理端');
INSERT INTO `sys_history_logs` VALUES (5, 1, '123.168.117.75', '修改菜单角色对应关系', '/system/acl-menu-role/update', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-11-02 08:55:53', '2023-11-02 06:59:12', '后台管理端');
INSERT INTO `sys_history_logs` VALUES (6, 1, '123.168.93.150', '更新用户', '/system/acl-user/update', NULL, '{\"status\":\"20000\",\"success\":true}', 'admin', '2023-11-10 17:49:38', '2023-11-10 09:49:38', '后台管理端');

SET FOREIGN_KEY_CHECKS = 1;
