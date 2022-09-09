from django.urls import path
from django.contrib.auth import views as auth_views
from .forms import UserLoginForm
from . import views as user_views


urlpatterns = [
   path('signup/', user_views.register, name='signup'),
   path('login/', auth_views.LoginView.as_view(
      template_name="authentication/login.html",
      authentication_form=UserLoginForm), name='login'),
   path('logout/', user_views.logout_user, name='logout'),
   path('password_reset/', auth_views.PasswordResetView.as_view(
      template_name="authentication/password_reset.html"),
      name='password_reset'),
   path('password_reset/done', auth_views.PasswordResetDoneView.as_view(
      template_name="authentication/password_reset_done.html"),
      name='password_reset_done'),
   path('password_reset_confirm/<uidb64>/<token>',
        auth_views.PasswordResetConfirmView.as_view(
           template_name="authentication/password_reset_confirm.html"),
        name='password_reset_confirm'),
   path('password_reset_complete',
        auth_views.PasswordResetCompleteView.as_view(
           template_name="authentication/password_reset_complete.html"),
        name='password_reset_complete'),
]
