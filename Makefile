LAZY = LazyDupList.java LazyInterface.java
LOCK = LockFreeDupList.java LockFreeInterface.java
OPTI = OptimisticDupList.java OptimisticInterface.java
TEST = TestLists.java ListTester.java
JFLAGS = java -ea

.PHONY: opti lazy lock tester

all: tester opti lazy lock 

tester:
	javac $(TEST)
opti:
	javac $(OPTI)
lazy:
	javac $(LAZY)
lock:
	javac $(LOCK)

run1: all
	$(JFLAGS) TestLists 1

run10: all
	$(JFLAGS) TestLists

run100: all
	$(JFLAGS) TestLists 100

run1000: all
	$(JFLAGS) TestLists 1000

clean:
	rm -rf *.class
