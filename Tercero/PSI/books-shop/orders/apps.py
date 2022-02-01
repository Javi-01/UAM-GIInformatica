from django.apps import AppConfig

'''
    @function: OrdersConfig
    @brief: Class in charge of the order configuration
    @author: Javier Fraile Iglesias
'''


class OrdersConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'orders'
