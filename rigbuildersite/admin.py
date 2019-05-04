from django.contrib import admin
from django import forms
from .models import *


class PartAdmin(admin.ModelAdmin):
    list_display = ('part_type', 'make', 'name', 'price', 'description', )


class BuildPartChoiceField(forms.ModelMultipleChoiceField):
    def label_from_instance(self, part):
        return str(part.part_type) + ' : ' + str(part) + ' - RM' + str(part.price)


class BuildAdmin(admin.ModelAdmin):
    list_display = ('build_usage', 'id', )
    filter_horizontal = ('parts',)

    def formfield_for_manytomany(self, db_field, request, **kwargs):
        if db_field.name == 'parts':
            f = BuildPartChoiceField(queryset=Part.objects.order_by('part_type__display_order', 'price').all())
            f.widget.attrs['size'] = 30
            return f
        return super().formfield_for_foreignkey(db_field, request, **kwargs)


admin.site.register(SiteInformation)
admin.site.register(Make)
admin.site.register(PartType)
admin.site.register(Part, PartAdmin)
admin.site.register(BuildUsage)
admin.site.register(Build, BuildAdmin)