FROM airhacks/glassfish
COPY ./target/pwblog.war ${DEPLOYMENT_DIR}
