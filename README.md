# auth-token-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Configuration and secrets

No credential is stored in this repository. Every value that used to be hardcoded is read from the environment, with local-only defaults:

| Variable | Purpose | Dev default in `application.properties` |
| --- | --- | --- |
| `DB_USER` | PostgreSQL user | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `postgres` |
| `DB_URL` | JDBC URL | `jdbc:postgresql://localhost:5432/abc_bank` |
| `JWT_PRIVATE_KEY_LOCATION` | RSA private key used to sign tokens | `privateKey.pem` (classpath) |
| `JWT_PUBLIC_KEY_LOCATION` | RSA public key used to verify tokens | `publicKey.pem` (classpath, committed) |
| `EXCHANGE_RATE_INFORMER_SECRET` | Secret for the `exchange-rate-informer` client | `change-me-dev-only` |

Local development (PowerShell):

```powershell
Copy-Item .env.example .env
# edit .env with your local values, then:
.\mvnw.cmd quarkus:dev
```

Packaged or deployed runs do not read `.env` — export real environment variables (or inject them from a secret store) instead:

```powershell
$env:DB_USER='app_user'; $env:DB_PASSWORD='***'; $env:DB_URL='jdbc:postgresql://db-host:5432/app_db'
$env:JWT_PRIVATE_KEY_LOCATION='C:/keys/privateKey.pem'; $env:JWT_PUBLIC_KEY_LOCATION='C:/keys/publicKey.pem'
$env:EXCHANGE_RATE_INFORMER_SECRET='***'
java -jar target/quarkus-app/quarkus-run.jar
```

## Generating JWT keys

The signing key is **not** committed. Generate your own pair; the private key stays outside version control (`.gitignore` covers `*.pem`, and only `src/main/resources/publicKey.pem` is explicitly re-included).

`openssl` is not on `PATH` on a stock Windows box; Git for Windows ships one:

```powershell
& "C:\Program Files\Git\usr\bin\openssl.exe" genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out privateKey.pem
& "C:\Program Files\Git\usr\bin\openssl.exe" rsa -pubout -in privateKey.pem -out publicKey.pem
```

For dev mode, place both files in `src/main/resources/`. For a packaged run, set `JWT_PRIVATE_KEY_LOCATION` and `JWT_PUBLIC_KEY_LOCATION` to their absolute paths. The two files must be a matching pair, otherwise issued tokens cannot be verified.

## Kubernetes deployment

`k8s/deployment.yaml` intentionally carries no credentials. Create the secrets and wire them in before rolling out:

```powershell
kubectl create secret generic auth-token-api-secrets -n cqrs-lab `
  --from-literal=DB_USER='<db-user>' `
  --from-literal=DB_PASSWORD='<db-password>' `
  --from-literal=DB_URL='jdbc:postgresql://<host>:5432/<database>' `
  --from-literal=EXCHANGE_RATE_INFORMER_SECRET='<client-secret>'

kubectl create secret generic auth-token-api-jwt -n cqrs-lab `
  --from-file=privateKey.pem=.\privateKey.pem `
  --from-file=publicKey.pem=.\publicKey.pem
```

Add to the container in `k8s/deployment.yaml`:

```yaml
          envFrom:
            - secretRef:
                name: auth-token-api-secrets
          env:
            - name: JWT_PRIVATE_KEY_LOCATION
              value: /deployments/keys/privateKey.pem
            - name: JWT_PUBLIC_KEY_LOCATION
              value: /deployments/keys/publicKey.pem
          volumeMounts:
            - name: jwt-keys
              mountPath: /deployments/keys
              readOnly: true
      volumes:
        - name: jwt-keys
          secret:
            secretName: auth-token-api-jwt
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/auth-token-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST resources for Hibernate ORM with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate Jakarta REST resources for your Hibernate Panache entities and repositories
- REST ([guide](https://quarkus.io/guides/rest)): Build RESTful web services and APIs using Jakarta REST (formerly JAX-RS)
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): Secure your applications with JSON Web Token
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC

## Provided Code

### REST Data with Panache

Generating Jakarta REST resources with Panache

[Related guide section...](https://quarkus.io/guides/rest-data-panache)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Rotating the exposed credentials

The previous revision of the repository published a live database password, a client secret and an RSA private key. Deleting them from the current tree is not enough — anything that was ever pushed must be considered compromised. Rotation is mandatory:

1. **Database password** — change the password for the PostgreSQL user and the JDBC URL's host/credentials where needed, then update the secret store and redeploy.
2. **Client secret** — issue a new `EXCHANGE_RATE_INFORMER_SECRET` and redistribute it to the `exchange-rate-informer` client.
3. **RSA key pair** — generate a new pair (see "Generating JWT keys") and publish the new public key. All tokens signed with the old key become untrusted.

After rotation, also purge the residual copies: GitHub keeps unreachable objects reachable by SHA for a period after a force-push and may retain them in caches/forks. Request a purge from GitHub Support if the repository was public.
