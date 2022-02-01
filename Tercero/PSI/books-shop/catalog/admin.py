from django.contrib import admin
from . import models


'''
    @function: BookAdmin
    @brief: Class in charge of book display configuration
    @author: Javier Fraile Iglesias
'''


class BookAdmin(admin.ModelAdmin):
    prepopulated_fields = {'slug': ('title',)}
    list_display = ('title', 'display_authors', 'score', 'date', 'price')


admin.site.register(models.Author)
admin.site.register(models.Book, BookAdmin)
admin.site.register(models.Comment)
