from django.db import models


class SiteInformation(models.Model):
    version = models.CharField(max_length=20)
    price_version = models.CharField(max_length=20)
    repo_url = models.URLField()


class Make(models.Model):
    name = models.CharField(max_length=50)
    thumbnail = models.URLField()

    def __str__(self):
        return self.name

    def as_dict(self):
        return {'name': self.name, 'thumbnail': self.thumbnail}


class PartType(models.Model):
    name = models.CharField(max_length=50)
    display_order = models.IntegerField()

    def __str__(self):
        return self.name

    @property
    def slug(self):
        return self.name.lower().replace(' ', '-')


class Part(models.Model):
    make = models.ForeignKey(Make, on_delete=models.CASCADE)
    part_type = models.ForeignKey(PartType, on_delete=models.CASCADE)
    name = models.CharField(max_length=100, blank=True)
    price = models.IntegerField()
    description = models.TextField(blank=True)
    product_page = models.URLField(blank=True)

    def __str__(self):
        return self.make.name+' '+self.name


class BuildUsage(models.Model):
    usage = models.CharField(max_length=100)
    display_order = models.IntegerField()

    def __str__(self):
        return self.usage


class Build(models.Model):
    build_usage = models.ForeignKey(BuildUsage, on_delete=models.CASCADE)
    parts = models.ManyToManyField(Part)

    def __str__(self):
        return str(self.build_usage)+' - '+str(self.id)
