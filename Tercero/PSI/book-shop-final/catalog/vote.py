from .models import Book, VoteBook


'''
    @function: add_book_score
    @brief: Function in charge of updating or creating the score of
    a book for a single user.
    @author: Javier Fraile Iglesias
'''


def add_user_vote(request, book, score):
    """update_or_create function is a django query function that create an
    entry on the model if it did not exist or update with the defaulta values
    """
    VoteBook.objects.update_or_create(
        book=book, user=request.user,
        defaults={'user_score': score},
    )

    """Calling the update Book model function"""
    update_book_score(book)


'''
    @function: update_book_score
    @brief: Function in charge of updating the score of the book model.
    @author: Javier Fraile Iglesias
'''


def update_book_score(book):
    votes = VoteBook.objects.filter(book=book)
    sum_votes = sum([v.user_score for v in votes])

    # Se actualiza el libro dado el id y la suma de votos obtenida en el bucle
    # de arriba y el numero de votos emitidos
    Book.objects.filter(id=book.id).update(
        score=(sum_votes/len(votes)), number_votes=len(votes))


'''
    @function: get_book_score
    @brief: Function in charge of getting the score of a book.
    @author: Javier Fraile Iglesias
'''


def get_book_score(book):
    return float(book.score) if book.score else 0.0
