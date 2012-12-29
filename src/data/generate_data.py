import random
import math

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*100))+", "+str(int(random.random()*100))+")"

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*100 + 200))+", "+str(int(random.random()*100) + 200)+")"

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*100 + 400))+", "+str(int(random.random()*100) + 400)+")"

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*500))+", "+str(int(random.random()*500))+")"

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*100 + 400))+", "+str(int(random.random()*100))+")"

# for i in range(1000):
#     print "TODO ("+str(int(random.random()*100 ))+", "+str(int(random.random()*100 + 400))+")"

def test_sin():
    for adder in [200, 400, 800]:
        for i in range(1000):
            print "TODO ("+str(i + adder)+","+str(int(math.sin(i/100.0) * 200 + adder))+")"

def test_eggs():

    radius = 50

    for location in [(100, 100), (200, 100), (150, 150)]:
        for i in range(2000):
            angle = random.random() * 360
            x = math.sin(angle) * radius
            y = math.cos(angle) * radius
            x *= random.random()
            # y *= random.random()
            print "TODO ("+str(int(x+location[0]))+", "+str(int(y+location[1]))+")"

def grid():
    for i in range(1000):
        for j in range(20):
            print "TODO ("+str(i)+", "+str(j+5)+")"

grid()