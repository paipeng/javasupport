#NOTICE that semicolon is on assignment line!
my $field_name = "name";
my $sql = <<SQL_STR;
SELECT 
	CategoryID as id,
	CategoryName as $field_name,
	Description as `desc`
FROM 
	Categories
SQL_STR
print $sql;

# To not have interpreted text, use 'quote' on token
my $field_name = "name";
my $sql = <<'SQL_STR';
SELECT 
	CategoryID as id,
	CategoryName as $field_name,
	Description as `desc`
FROM 
	Categories
SQL_STR
print $sql;
