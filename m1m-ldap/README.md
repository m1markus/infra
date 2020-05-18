# OpenLDAP

Build your own customized OpenLDAP docker image.

## Infos from
- https://github.com/osixia/docker-openldap
- https://medium.com/@wshihadeh/ldap-docker-image-with-populated-users-3a5b4d090aa4
- https://www.adimian.com/blog/2014/10/how-to-enable-memberof-using-openldap/
- https://www.digitalocean.com/community/tutorials/how-to-manage-and-use-ldap-servers-with-openldap-utilities



# Build
```
docker build -t m1m-ldap .
```


# Managing

## Start
```
docker run -p 389:389 -p 636:636 --rm --name ldap-m1m --detach m1m-ldap
```

## Stop
```
docker stop ldap-m1m
docker rm ldap-m1m      (not needed when run with flag ... --rm)
```

## List content
```
docker exec ldap-m1m ldapsearch -x -H ldap://localhost -b dc=m1m,dc=ch -D "cn=admin,dc=m1m,dc=ch" -w toSecret2beTrue,2022
```

### Query a specific user (cn=mue)
```
docker exec ldap-m1m ldapsearch -x -H ldap://localhost -b dc=m1m,dc=ch -D "cn=admin,dc=m1m,dc=ch" -w toSecret2beTrue,2022 "(&(objectClass=inetOrgPerson)(cn=mue))"
```

### Query all groups from a user
```
docker exec ldap-m1m ldapsearch -x -H ldap://localhost -b dc=m1m,dc=ch -D "cn=admin,dc=m1m,dc=ch" -w toSecret2beTrue,2022 "(&(objectClass=inetOrgPerson)(cn=mue))" memberOf
```

### Execute commands in the container

docker exec -it ldap-m1m bash
 
- ldapadd -Y EXTERNAL -H ldapi:/// -f /tmp/10-memberof.ldif

- ldapmodify -Y EXTERNAL -H ldapi:/// -f /tmp/11-memberof.ldif


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
