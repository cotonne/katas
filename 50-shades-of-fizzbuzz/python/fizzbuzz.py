import tensorflow as tf
import numpy as np

def fizzbuzz(i):
    if i%15==0 : return [1, 1, 0]
    if i% 5==0: return [0, 1, 0]
    if i% 3==0: return [1, 0, 0]
    return [0, 0, 1]

# inspired by https://github.com/joelgrus/fizz-buzz-tensorflow

max_val = 128
max_len = 10
X = list(range(1, max_val + 1))
Y = np.float32(np.array(list(map(fizzbuzz, X))).reshape(128,3))
X = [ [int(x) for x in bin(i)[2:] ] for i in range(max_val)] 
X = np.asarray([np.pad(a, (max_len - len(a), 0), 'constant', constant_values=0) for a in X])

#X = np.float32(np.transpose(X).reshape(128, 5))

# Solution 1 : Calculation
#x = tf.placeholder(tf.float32, [None, 5])
#W = tf.Variable(tf.zeros([5, 3]))
#b = tf.Variable(tf.zeros([3]))
#y = tf.matmul(x, W) + b
#y_ = tf.placeholder(tf.int64, [None])
#cross_entropy = tf.nn.softmax_cross_entropy_with_logits_v2(labels=y_, logits=y)
#train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

## Train
#for i in range(len(Y)):
#  batch_xs, batch_ys = np.transpose(X[i]), Y[i]
#  sess.run(train_step, feed_dict={x: batch_xs, y_: batch_ys})
#
## Test trained model
#correct_prediction = tf.equal(tf.argmax(y, 1), y_)
#accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
#print(sess.run(
#      accuracy, feed_dict={
#          x: X,
#          y_: Y
#      }))


# Solution 2 : dense
y_ = tf.placeholder(tf.float32, [None, 3], "output")
x = tf.placeholder(tf.float32, [None, max_len], "input")
first_hidden_layer = tf.layers.dense(inputs=x, units=5, activation=tf.nn.relu)
logits = tf.layers.dense(first_hidden_layer, units=3)
softmax = tf.nn.softmax(logits)
# Don't use cross entropy except you have a disjonctif output!!!
# cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits_v2(labels=y_, logits=logits))
mean_squared_error = tf.losses.mean_squared_error(labels=y_, predictions=softmax)
train_step = tf.train.GradientDescentOptimizer(0.05).minimize(mean_squared_error)


sess = tf.InteractiveSession()
tf.global_variables_initializer().run()

graph_location = "log/"
print('Saving graph to: %s' % graph_location)
train_writer = tf.summary.FileWriter(graph_location)

for epoch in range(100):
    p = np.random.permutation(range(len(X)))
    X, Y = X[p], Y[p]
    BATCH_SIZE=16
    for i in range(1, max_val, BATCH_SIZE):
      batch_xs, batch_ys = X[i:i+BATCH_SIZE], Y[i:i+BATCH_SIZE]
      sess.run(train_step, feed_dict={x: batch_xs, y_: batch_ys})
    vectors = softmax.eval(feed_dict={x: X})
    normalize = list(map(lambda line: list(map(lambda x: 0 if x < 0.33 else 1, line)), vectors))
    print(epoch, np.mean(Y == normalize))
    
# Don't use argmax  except you have a disjonctif output!!!
# one_hot = tf.argmax(logits, 1)
#vectors = softmax.eval(feed_dict={x: X})
#print("accuracy = {}".format(vectors))
print(vectors[1:16])


train_writer.add_graph(tf.get_default_graph())

