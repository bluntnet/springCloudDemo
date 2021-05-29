DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `phone` varchar(20) DEFAULT NULL COMMENT  '手机号',
  `role` INTEGER DEFAULT 0,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';
# 默认密码为 admin
INSERT INTO `account` VALUES ('0', 'admin', '$2a$10$AV2.HHsU/nbPXD5O4th0D.9jKkFf1M9QSodClK8ViR6e7E1f2exRe',null, '0');
INSERT INTO `account` VALUES ('1', 'user', '$2a$10$1HQnkO6eg5P.m5ZUbLOAHeGdk.AYAH72gW4WbzfppXLvgnDIjJQpG',null, '1');