from django.apps import AppConfig


'''
    @function: AuthenticationConfi
    @brief: Class in charge of the identification configuration
    @author: Javier Fraile Iglesias
'''


class AuthenticationConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'authentication'
