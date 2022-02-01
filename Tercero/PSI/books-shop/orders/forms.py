from django import forms
from .models import Order


class CartAddBookForm(forms.Form):

    quantity = forms.TypedChoiceField(
        coerce=int,
        choices=[(x, x) for x in range(1, 21)],
        required=True
    )


class OrderCreateForm(forms.ModelForm):
    class Meta:
        model = Order
        fields = [
            'first_name', 'last_name', 'email',
            'address', 'postal_code', 'city',
            ]
        widgets = {
            'first_name': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'id': 'first_name',
                    'placeholder': 'First Name'
                    }
                ),
            'last_name': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'id': 'last_name',
                    'placeholder': 'Last Name'
                    }
                ),
            'email': forms.EmailInput(
                attrs={
                    'class': 'form-control',
                    'id': 'email',
                    'placeholder': 'Email'
                    }
                ),
            'address': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'id': 'address',
                    'placeholder': 'Address'
                    }
                ),
            'postal_code': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'id': 'postal_code',
                    'placeholder': 'Postal Code'
                    }
                ),
            'city': forms.TextInput(
                attrs={
                    'class': 'form-control',
                    'id': 'city',
                    'placeholder': 'City'
                    }
                ),
            }
