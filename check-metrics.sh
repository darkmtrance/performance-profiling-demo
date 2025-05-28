#!/bin/bash

# Colors for better readability
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Fetching and analyzing Prometheus metrics...${NC}\n"

# Function to print section headers
print_header() {
    echo -e "\n${YELLOW}=== $1 ===${NC}"
}

# Function to format and calculate metric values
get_metric_value() {
    local metric=$1
    local filter=$2
    curl -s http://localhost:8080/actuator/prometheus | grep "^$metric" ${filter:+"| $filter"} | awk '{print $2}' | head -n 1
}

# Function to format response time metrics
format_response_time() {
    local metric_name=$1
    local count=$(get_metric_value "${metric_name}_seconds_count")
    local max=$(get_metric_value "${metric_name}_seconds_max")
    local sum=$(get_metric_value "${metric_name}_seconds_sum")
    local p95=$(get_metric_value "${metric_name}_seconds{quantile=\"0.95\"}")
    
    echo -e "${BLUE}Endpoint:${NC} $metric_name"
    echo -e "${BLUE}Total Requests:${NC} ${count:-0}"
    echo -e "${BLUE}Max Response Time:${NC} ${max:-0}s"
    if [ ! -z "$sum" ] && [ ! -z "$count" ] && [ "$count" != "0" ]; then
        avg=$(awk "BEGIN {printf \"%.3f\", $sum/$count}")
        echo -e "${BLUE}Average Response Time:${NC} ${avg}s"
    fi
    [ ! -z "$p95" ] && echo -e "${BLUE}95th Percentile:${NC} ${p95}s"
    echo
}

# Performance Summary
print_header "Performance Summary"
uptime=$(get_metric_value "process_uptime_seconds")
echo -e "${BLUE}Application Uptime:${NC} $(printf "%.2f" $uptime) seconds"

# HTTP Endpoints Performance
print_header "HTTP Endpoints Performance"
format_response_time "demo.api.login"
format_response_time "demo.api.complex"
format_response_time "demo.api.logout"

# Memory Usage
print_header "Memory Usage"
heap_used=$(get_metric_value "jvm_memory_used_bytes{area=\"heap\"}")
heap_used_mb=$(awk "BEGIN {printf \"%.2f\", $heap_used/1024/1024}")
echo -e "${BLUE}Heap Memory Used:${NC} ${heap_used_mb}MB"

eden_used=$(get_metric_value "jvm_memory_used_bytes{id=\"Eden Space\"}")
eden_used_mb=$(awk "BEGIN {printf \"%.2f\", $eden_used/1024/1024}")
echo -e "${BLUE}Eden Space Used:${NC} ${eden_used_mb}MB"

old_gen_used=$(get_metric_value "jvm_memory_used_bytes{id=\"Old Gen\"}")
old_gen_used_mb=$(awk "BEGIN {printf \"%.2f\", $old_gen_used/1024/1024}")
echo -e "${BLUE}Old Gen Used:${NC} ${old_gen_used_mb}MB"

# Thread and GC Stats
print_header "Thread and GC Statistics"
threads=$(get_metric_value "jvm_threads_live_threads")
daemon_threads=$(get_metric_value "jvm_threads_daemon_threads")
echo -e "${BLUE}Live Threads:${NC} $threads (${daemon_threads} daemon)"

gc_count=$(get_metric_value "jvm_gc_collection_seconds_count")
gc_time=$(get_metric_value "jvm_gc_collection_seconds_sum")
echo -e "${BLUE}GC Pauses:${NC} ${gc_count:-0}"
echo -e "${BLUE}Total GC Time:${NC} ${gc_time:-0}s"

# System Resource Usage
print_header "System Resource Usage"
sys_cpu=$(get_metric_value "system_cpu_usage")
proc_cpu=$(get_metric_value "process_cpu_usage")
sys_load=$(get_metric_value "system_load_average_1m")

echo -e "${BLUE}System CPU Usage:${NC} $(awk "BEGIN {printf \"%.2f%%\", $sys_cpu * 100}")"
echo -e "${BLUE}Process CPU Usage:${NC} $(awk "BEGIN {printf \"%.2f%%\", $proc_cpu * 100}")"
echo -e "${BLUE}System Load (1m):${NC} $sys_load"

# Custom Application Metrics
print_header "Custom Application Metrics"
active_users=$(get_metric_value "application_users_active")
login_failures=$(get_metric_value "application_login_failures_total")
complex_ops=$(get_metric_value "application_operation_complex_seconds_count")

echo -e "${BLUE}Active Users:${NC} ${active_users:-0}"
echo -e "${BLUE}Login Failures:${NC} ${login_failures:-0}"
echo -e "${BLUE}Complex Operations:${NC} ${complex_ops:-0}"
