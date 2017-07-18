#!/bin/bash

set -e

# setup environment
source /marv_env.sh

# execute command
exec "$@"
