See [DaoMessage](https://github.com/yakworks/gorm-tools/blob/master/plugin/src/main/groovy/grails/plugin/dao/DaoMessage.groovy)

Gorm-tools provides a way to build message maps with information about a status of a domain instance.
Uses i18n messages.

## Saved and no saved messages

The example below shows how to build ```saved``` message for a domain:

```groovy

    User user = new User(id:100,version:1)

    Map msg = DaoMessage.saved(user)
    assert 'default.saved.message' == msg.code //i18 code
    assert 100 == msg.args[1]

```

## List of available messages

* saved
* not saved
* updated
* not updated
* deleted
* not deleted
* notFound
* optimisticLockingFailure - Another user has updated the resource while you were editing

Gorm-tools provides its own types of exceptions to handle errors which relate to domains.

## DomainException
See [DomainException](https://github.com/yakworks/gorm-tools/blob/master/plugin/src/main/groovy/grails/plugin/dao/DomainException.groovy)

An extension of the default ValidationException. It is possible to pass the entity and the message map.

## DomainNotFoundException
See [DomainNotFoundException](https://github.com/yakworks/gorm-tools/blob/master/plugin/src/main/groovy/grails/plugin/dao/DomainNotFoundException.groovy)

An extension of the DomainException to be able to handle rest request which should respond with 404 error.