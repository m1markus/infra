# OpenLDAP

Build your own customized OpenLDAP docker image.

## Infos from
https://medium.com/@wshihadeh/ldap-docker-image-with-populated-users-3a5b4d090aa4
https://github.com/osixia/docker-openldap
https://www.adimian.com/blog/2014/10/how-to-enable-memberof-using-openldap/



# Build
```
docker build -t m1m-ldap .
```


# Managing

## Start
```
docker run -p 389:389 -p 636:636 --name ldap-m1m --detach m1m-ldap
```

## Stop
```
docker stop ldap-m1m
docker rm ldap-m1m
```

## List content
```
docker exec ldap-m1m ldapsearch -x -H ldap://localhost -b dc=m1m,dc=ch -D "cn=admin,dc=m1m,dc=ch" -w toSecret2beTrue,2022
```

## Encript password
```
docker exec ldap-m1m slappasswd -h {SHA} -s my_secret_password
```

# ldap UI

## ui from https://github.com/osixia/docker-phpLDAPadmin

docker run -p 6443:443 --name ldap-ui \
        --env PHPLDAPADMIN_LDAP_HOSTS=localhost \
        --detach osixia/phpldapadmin:0.9.0

## login
https://localhost:6443  
``` 
login DN: cn=admin,dc=m1m,dc=ch
password: see_in_your_config    
```
