#!/usr/bin/env bash
# Maven wrapper script for Ambrose
# This script ensures reproducible builds by using a controlled Maven invocation

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Use the system Maven 3.9+ if available, otherwise fall back
if command -v mvn &>/dev/null; then
  exec mvn "$@"
else
  echo "Error: Maven (mvn) is not installed or not in PATH." >&2
  exit 1
fi