# Array of numbers
@n_ls = (0, 1, 2, 3, 4);
print("array compared: ", @n_ls eq (0, 1, 2, 3, 4), "\n"); # can't do!
print("range compared: ", @n_ls eq (0..5), "\n");          # can't do!
@n_ls2 = (0, 1, 2, 3, 4);
print("array2 compared: ", @n_ls eq @n_ls2, "\n");         # this works! hum....

# Hash
%h1 = ("a" => 1, "b" => 2, "c" => 3);
%h2 = (a => 1, b => 2, c => 3);
print(%h2 eq %h2, "\n"); # true?

