# Inventory service
###### Сервис управления товарами является микросервисом, предназначенным для обработки операций, связанных товаром. 
#### Используемые технологии:

###### Java 17
###### Spring Boot Starter Data JPA
###### Jackson
###### Flyway Core
###### Spring Kafka
###### PostgreSQL
###### ModelMapper

#### Функциональность

###### Класс ProductController является компонентом Spring для управления продуктами. Он взаимодействует с другими компонентами, такими как ProductService, Producer и ProductOrderService. Некоторые из его методов включают:

###### findAll(): Получает все товары и отправляет сообщение с результатом в топик inventoryResponseTopic.
###### findAllProductsByOrderId(int orderId): Получает все товары по идентификатору заказа и отправляет сообщение в топик inventoryResponseTopic.
###### findById(int id): Находит товар по идентификатору и отправляет сообщение в топик inventoryResponseTopic.
###### save(ProductDTO productDTO): Сохраняет новый товар.
###### addProductToOrder(ProductOrderDTO productOrderDTO): Добавляет товар в заказ и отправляет сообщение в топик inventoryResponseTopic.
###### update(ProductDTO productDTO): Обновляет информацию о товаре.
###### updateProductQuantityInOrder(ProductOrderDTO productOrderDTO): Обновляет количество товара в заказе и отправляет сообщение в топик inventoryResponseTopic.
###### delete(int id): Удаляет продукт по идентификатору.
###### deleteByOrderIdAndProductId(int orderId, int productId): Удаляет товар из заказа по идентификаторам заказа и продукта.
###### deleteAllProductsInOrderByOrderId(int orderId): Удаляет все товары в заказе по идентификатору заказа.

## Настройка и развертывание

###### Клонируйте репозиторий проекта.
###### Установите все необходимые зависимости, указанные в файле pom.xml.
###### У Вас должна быть запущена Kafka и Docker.
###### Выполните команду "docker-compose up" в теминале, находясь в корневой папке сервиса.
###### Запустите сервис на вашем сервере или локальной машине.
###### Проверьте работоспособность сервиса, обращаясь к соответствующим url в UserOrchestrationService или Frontend.
###### Зайти сможете по логину и паролю admin