from django.shortcuts import render
from django.http import HttpResponse
from .models import *
import json, math


def index(request):
    pts = PartType.objects.order_by('display_order').all()
    parts = Part.objects.prefetch_related('part_type', 'make').order_by('price')

    builds = Build.objects.prefetch_related('parts', 'build_usage').all()
    formatted_builds = {}
    for build in builds:
        price = sum(x.price for x in build.parts.all())
        if build.build_usage.usage not in formatted_builds.keys():
            formatted_builds[build.build_usage.usage] = []
        formatted_builds[build.build_usage.usage].append({'id': build.id, 'price': int(math.ceil(price/100.0)*100)})
    for key, build_list in formatted_builds.items():
        build_list.sort(key=lambda x: (x['price'], x['id']))

    context = {'part_types': {x:parts.filter(part_type=x).all() for x in pts},
               'site_info': SiteInformation.objects.first(),
               'formatted_builds': formatted_builds}
    return render(request, 'rigbuildersite/index.html', context)


def index_data(request):
    parts = Part.objects.prefetch_related('part_type', 'make').all()
    parts_data = {}
    for part in parts:
        parts_data[part.id]= {
            'name': str(part),
            'price': part.price,
            'description': part.description,
            'product-page' : part.product_page,
            'make-thumbnail': part.make.thumbnail,
            'part-type': part.part_type.slug,
        }

    na_parts = {x.slug: parts.filter(make__name='N/A', part_type=x).first().id for x in PartType.objects.all()}

    builds = Build.objects.prefetch_related('parts', 'parts__part_type').all()
    builds_data = {build.id:{part.part_type.slug:part.id for part in build.parts.all()} for build in builds}

    context = {
                'parts': parts_data,
                'builds': builds_data,
                'na_parts': na_parts,
               }
    return HttpResponse(json.dumps(context))