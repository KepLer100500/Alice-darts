## Alice-darts
### Навык для Яндекс диалогов - Игра в дартс
<img src="https://img.shields.io/github/actions/workflow/status/KepLer100500/Alice-darts/test.yml?style=plastic&logo=spring&logoColor=green&label=Tests">

Для публикации навыка потребуется ssl сертификат.

* Можно использовать letsencrypt + certbot:

`sudo certbot`

`cd /etc/letsencrypt/live/your-site-name.ru/`

* Создание хранилища ключей с сертификатами - keystore.p12

`openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name kepler -CAfile chain.pem -caname root`

* Созданный keystore.p12 необходимо будет поместить в дирректорию:

`Alice-darts\Dialog\src\main\resources`
