package gorm.tools.dao;

public enum DaoEventType {
    BeforeRemove,
    BeforeCreate,
    BeforeUpdate,
    AfterRemove,
    AfterCreate,
    AfterUpdate,
    BeforePersist,
    AfterPersist
}