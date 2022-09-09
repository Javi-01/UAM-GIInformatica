from decimal import Decimal
from django.test import TestCase, Client
from django.contrib.auth.models import User

from .models import VoteBook, Book


class JuneTest(TestCase):
    def setUp(self):
        Book.objects.create(
            isbn='1234567890123',
            title='title_1',
            price=Decimal(23.32),
            path_to_cover_image='kk.jpg',
            number_copies_stock=23,
        )
        self.user1 = User.objects.create_user(
            username='pedrobue',
            password='beuno12345',
            first_name='Pedro',
            last_name='Bueno',
            email='p.bueno@gmail.com',
        )
        self.user2 = User.objects.create_user(
            username='pmarmol',
            password='troncomovil',
            first_name='Pablo',
            last_name='Marmol',
            email='p.marmol@cantera.com'
        )

    def test01_score_book(self):

        # First check that the book has not scored yet, and the number of votes
        # is the default with 0
        self.assertIsNone(Book.objects.get(title="title_1").score)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 0)

        # Login the client
        client = Client()
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        # Vote thew book via form with a score of 6
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '6'})

        # Check that the book has been updated
        self.assertEquals(Book.objects.get(title="title_1").score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)
        # Check that the vote book register was created
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 6.0)

    def test02_two_users_score(self):
        # Login the first client
        client = Client()
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        # Vote thew book via form with a score of 5
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '5'})

        # Check that the book has been updated
        self.assertEquals(Book.objects.get(title="title_1").score, 5.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)

        client.logout()
        # Login the second client
        self.assertTrue(client.login(
            username='pedrobue', password='beuno12345'))

        # Vote thew book via form with a score of 7
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '7'})

        # Check that the avarage of the two votes is (5+7)/2 = 6
        self.assertEquals(Book.objects.get(title="title_1").score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)

    def test03_repeated_score(self):

        # Login the client
        client = Client()
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        # Vote thew book via form with a score of 6
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '6'})

        # Check that the user vote was added
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)

        # Vote other time the same book but now with a score of 8
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '8'})

        # Check that the user vote wwas updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 8.0)
        # Check now in the book model that this was updated
        self.assertEquals(Book.objects.get(title="title_1").score, 8.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)

    def test04_two_users__score_and_repeat_one_score(self):

        # Login the client
        client = Client()
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        # Vote thew book via form with a score of 6
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '6'})

        # Check that the user vote was added
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 6.0)
        # Check now in the book model that this was updated
        self.assertEquals(Book.objects.get(title="title_1").score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)

        client.logout()
        # Login the second client
        self.assertTrue(client.login(
            username='pedrobue', password='beuno12345'))
        # Vote with this user with a score of 8
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '8'})

        # Check that the user vote wwas updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user1).user_score, 8.0)
        # Check that the avarage change with (8+6)/2 = 7
        self.assertEquals(Book.objects.get(title="title_1").score, 7.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)

        # Finally change the vote with the second user
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '9'})

        # Check that the user vote wwas updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user1).user_score, 9.0)
        # Check that the avarage change with (9+6)/2 = 7.5
        self.assertEquals(Book.objects.get(title="title_1").score, 7.5)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)

    def test05_two_users_repeate_both_scores(self):

        # Login the client
        client = Client()
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        # Vote thew book via form with a score of 6
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '6'})

        # Check that the user vote was added
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 6.0)
        # Check now in the book model that this was updated
        self.assertEquals(Book.objects.get(title="title_1").score, 6.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 1)

        client.logout()
        # Login the second client
        self.assertTrue(client.login(
            username='pedrobue', password='beuno12345'))
        # Vote with this user with a score of 8
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '8'})

        # Check that the user vote wwas updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user1).user_score, 8.0)
        # Check that the avarage change with (8+6)/2 = 7
        self.assertEquals(Book.objects.get(title="title_1").score, 7.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)

        # Finally change the vote with the second user
        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '9'})

        # Check that the user vote wwas updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user1).user_score, 9.0)
        # Check that the avarage change with (9+6)/2 = 7.5
        self.assertEquals(Book.objects.get(title="title_1").score, 7.5)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)

        client.logout()
        # Login the first client to change his vote
        self.assertTrue(client.login(
            username='pmarmol', password='troncomovil'))

        _ = client.post(
            "/home/vote_book/" +
            Book.objects.get(title="title_1").slug, {'score': '5'})

        # Check that the user vote was updated
        self.assertEquals(VoteBook.objects.get(
            user=self.user2).user_score, 5.0)
        #  Check that the avarage change with (9+5)/2 = 7
        self.assertEquals(Book.objects.get(title="title_1").score, 7.0)
        self.assertEquals(Book.objects.get(title="title_1").number_votes, 2)
