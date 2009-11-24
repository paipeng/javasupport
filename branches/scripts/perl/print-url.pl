use warnings;
use strict;

# Create a user agent object
use LWP::UserAgent;
my $ua = LWP::UserAgent->new;
#$ua->agent("MyApp/0.1 ");

# Create a request
my $url = $ARGV[0] || die "Enter a url argument like: http://search.cpan.org";
my $req = HTTP::Request->new(GET => $url);
$req->content_type('application/x-www-form-urlencoded');
$req->content('query=libwww-perl&mode=dist');

# Pass request to the user agent and get a response back
my $res = $ua->request($req);

# Check the outcome of the response
if ($res->is_success) {
	print $res->content;
} else {
	print $res->status_line, "\n";
}
