FROM osixia/openldap:1.3.0
LABEL maintainer="noreply@m1m.ch"
ENV LDAP_ORGANISATION="mmm" \
    LDAP_DOMAIN="m1m.ch" \
    LDAP_ADMIN_PASSWORD="toSecret2beTrue,2022"
COPY bootstrap.ldif /container/service/slapd/assets/config/bootstrap/ldif/50-bootstrap.ldif
