#!/usr/bin/env python
# -*- coding: utf-8 -*-

from setuptools import setup

with open('README.rst') as readme_file:
    readme = readme_file.read()

with open('HISTORY.rst') as history_file:
    history = history_file.read()

requirements = [
    # TODO: put package requirements here
]

test_requirements = [
    # TODO: put package test requirements here
]

setup(
    name='gobii_mde',
    version='0.1.0',
    description="The MetaData Extractors. Python project for extracting metadata information to tab-delimited files.",
    long_description=readme + '\n\n' + history,
    author="Kevin Palis",
    author_email='kdp44@cornell.edu',
    url='http://cbsugobii05.tc.cornell.edu:6083/scm/gm/data-warehouse-postgresql',
    packages=[
        'gobii_mde', 'gobii_mde.db', 'gobii_mde.util'
    ],
    package_dir={'gobii_mde':
                 'gobii_mde', 'gobii_mde.db': 'gobii_mde/db', 'gobii_mde.util': 'gobii_mde/util'},
    include_package_data=True,
    #package_data={'gobii_mde/res/map': ['*']},
    install_requires=requirements,
    license="GNU General Public License v3",
    zip_safe=False,
    keywords='gobii_mde',
    classifiers=[
        'Development Status :: 2 - Pre-Alpha',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: GNU General Public License',
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
