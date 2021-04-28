DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `password` varchar(20) DEFAULT NULL COMMENT  '手机号',
  `role` INTEGER DEFAULT 0,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

INSERT INTO `account` VALUES ('0', 'admin', '$2a$10$7HMxhcXQXp5vtVm.fmWyK.NUYNrB1.6/FUfq3PiFnOnenCp/CVIDa', '1');