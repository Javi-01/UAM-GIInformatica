from django import forms
from django.contrib.auth.models import User
from django.contrib.auth.forms import (
    UserCreationForm,
    AuthenticationForm,
    UsernameField)


'''
    @function: UserRegistrerFor
    @brief: Class in charge of generating the registration form
    @author: Javier Fraile Iglesias
'''


class UserRegistrerForm(UserCreationForm):
    first_name = forms.CharField(max_length=50, widget=forms.TextInput(
        attrs={
            'class': 'form-control',
            'placeholder': 'First Name',
            'id': 'first_name',
            'type': 'text',
            'required': 'true',
            }
        )
    )
    last_name = forms.CharField(max_length=50, widget=forms.TextInput(
        attrs={
            'class': 'form-control',
            'placeholder': 'Last Name',
            'id': 'last_name',
            'type': 'text',
            'required': 'true',
            }
        )
    )
    email = forms.EmailField(widget=forms.EmailInput(
        attrs={
            'class': 'form-control',
            'id': 'email',
            'placeholder': 'Email',
            'type': 'email',
            'required': 'true',
            }
        )
    )
    password1 = forms.CharField(widget=forms.PasswordInput(
        attrs={
            'class': 'form-control',
            'type': 'password',
            'id': 'password1',
            'placeholder': 'Password',
            'required': 'true',
            }
        ),
        label=("Password"),
    )
    password2 = forms.CharField(widget=forms.PasswordInput(
        attrs={
            'class': 'form-control',
            'type': 'password',
            'id': 'password2',
            'placeholder': 'Confirm Password',
            'required': 'true',
            }
        ), label=("Password confirmation")
    )

    class Meta:
        model = User
        fields = [
            'first_name', 'last_name', 'username',
            'email', 'password1', 'password2',
            ]
        widgets = {
            'username': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'placeholder': 'Username',
                    'id': 'username',
                    }
                )
            }


'''
    @function: UserLoginForm
    @brief: Class in charge of generating the login form
    @author: Javier Fraile Iglesias
'''


class UserLoginForm(AuthenticationForm):
    def __init__(self, *args, **kwargs):
        super(UserLoginForm, self).__init__(*args, **kwargs)

    username = UsernameField(widget=forms.TextInput(
        attrs={
            'class': 'form-control',
            'placeholder': 'Username',
            'id': 'username'
            }
        ),
        label=("Username"),
    )
    password = forms.CharField(widget=forms.PasswordInput(
        attrs={
            'class': 'form-control',
            'placeholder': 'Password',
            'id': 'password',
            }
        ),
        label=("Password"),
    )
