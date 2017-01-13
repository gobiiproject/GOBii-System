#!/usr/bin/env python
# -*- coding: utf-8 -*-


try:
    from setuptools import setup
except ImportError:
    from distutils.core import setup


with open('README.rst') as readme_file:
    readme = readme_file.read()

with open('HISTORY.rst') as history_file:
    history = history_file.read()

requirements = [
    # TODO: put package requirements here
    'psycopg2==2.6.1'
]

test_requirements = [
    # TODO: put package test requirements here
]

setup(
    name='gobii_ifl',
    version='0.1.0',
    description="This is the GOBII Intermediate File Loader project. Modules here will take in the intermediate file output of the Digesters and process them accordingly before loading into their corresponding tables in the GOBII Flatmeta schema.",
    long_description=readme + '\n\n' + history,
    author="Kevin Palis",
    author_email='kdp44@cornell.edu',
    url='http://cbsugobii05.tc.cornell.edu:6083/scm/gm/data-warehouse-postgresql',
    packages=[
        'gobii_ifl', 'gobii_ifl.db', 'gobii_ifl.res', 'gobii_ifl.res.map', 'gobii_ifl.util'
    ],
    package_dir={'gobii_ifl':
                 'gobii_ifl', 'gobii_ifl.db': 'gobii_ifl/db', 'gobii_ifl.res': 'gobii_ifl/res', 'gobii_ifl.res.map': 'gobii_ifl/res/map', 'gobii_ifl.util': 'gobii_ifl/util'},
    include_package_data=True,
    package_data={'gobii_ifl/res/map': ['*']},
    install_requires=requirements,
    license="ISCL",
    zip_safe=False,
    keywords='gobii_ifl',
    classifiers=[
        'Development Status :: 2 - Pre-Alpha',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: ISC License (ISCL)',
        'Natural Language :: English',
        "Programming Language :: Python :: 2",
        'Programming Language :: Python :: 2.6',
        'Programming Language :: Python :: 2.7',
        'Programming Language :: Python :: 3',
        'Programming Language :: Python :: 3.3',
        'Programming Language :: Python :: 3.4',
        'Programming Language :: Python :: 3.5',
    ],
    test_suite='tests',
    tests_require=test_requirements
)
