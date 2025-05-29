#!/bin/bash

# Colors for better readability
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting Distributed Tracing Demo${NC}\n"

# Function to make a request and measure time
make_request() {
    local endpoint=$1
    local description=$2
    echo -e "${BLUE}Testing: ${description}${NC}"
    time curl -s "http://localhost:8080/demo.api/distributed/$endpoint/test"
    echo -e "\n${YELLOW}Check Zipkin UI (http://localhost:9411) to see the trace${NC}\n"
    sleep 2
}

# Test sequential calls (anti-pattern)
make_request "sequential" "Sequential Calls (Anti-pattern)"

# Test parallel calls (good practice)
make_request "parallel" "Parallel Calls (Good Practice)"

# Test chained calls (anti-pattern)
make_request "chained" "Chained Calls (Anti-pattern)"

# Test reactive calls (good practice)
make_request "reactive" "Reactive Calls (Good Practice)"

# Run full simulation
echo -e "${GREEN}Running Full Simulation${NC}"
curl -s "http://localhost:8080/demo.api/distributed/simulate"

echo -e "\n${YELLOW}Demo complete. Visit http://localhost:9411 to analyze the traces${NC}"
