from django.apps import AppConfig

'''
    @function: CatalogConfig
    @brief: Class in charge of the catalog configuration
    @author: Javier Fraile Iglesias
'''


class CatalogConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'catalog'
