# Generated by Django 2.2.1 on 2019-05-03 11:00

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('rigbuildersite', '0002_auto_20190502_2054'),
    ]

    operations = [
        migrations.AlterField(
            model_name='make',
            name='thumbnail',
            field=models.URLField(),
        ),
        migrations.AlterField(
            model_name='part',
            name='product_page',
            field=models.URLField(blank=True),
        ),
        migrations.AlterField(
            model_name='siteinformation',
            name='repo_url',
            field=models.URLField(),
        ),
    ]