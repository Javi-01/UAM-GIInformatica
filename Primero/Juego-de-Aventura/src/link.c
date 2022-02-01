#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "types.h"
#include "link.h"

struct _Link /*!< Link structure*/
{
    Id id;                    /*!< Identificador del enlace */
    char name[WORD_SIZE + 1]; /*!< Nombre del enlace */
    Id firstId;               /*!< Primer identificador del enlace creado */
    Id secondId;              /*!< Segundo identificador del enlace creado */
    int status;               /*!< Estado del enlace creado */
};

Link *link_create(Id id)
{

    Link *newLink = NULL;

    if (id == NO_ID)
        return NULL;

    newLink = (Link *)malloc(sizeof(Link));

    if (newLink == NULL)
    {
        return NULL;
    }
    newLink->id = id;

    newLink->name[0] = '\0';

    newLink->firstId = NO_ID;
    newLink->secondId = NO_ID;

    newLink->status = CERRADO;

    return newLink;
}

STATUS link_destroy(Link *link)
{
    if (!link)
    {
        return ERROR;
    }

    free(link);
    link = NULL;

    return OK;
}

STATUS link_set_name(Link *link, char *name)
{
    if (!link || !name)
    {
        return ERROR;
    }

    if (!strcpy(link->name, name))
    {
        return ERROR;
    }

    return OK;
}

STATUS link_set_first_id(Link *link, Id id)
{
    if (!link || id == NO_ID)
    {
        return ERROR;
    }
    link->firstId = id;
    return OK;
}

STATUS link_set_second_id(Link *link, Id id)
{
    if (!link || id == NO_ID)
    {
        return ERROR;
    }
    link->secondId = id;
    return OK;
}

STATUS link_set_status(Link *link, int status)
{

    if (!link || ((status != 0) && (status != 1)))
    {
        return ERROR;
    }
    link->status = status;
    return OK;
}

Id link_get_id(Link *link)
{
    if (!link)
    {
        return NO_ID;
    }
    return link->id;
}

Id link_get_first_id(Link *link)
{
    if (!link)
    {
        return NO_ID;
    }
    return link->firstId;
}

Id link_get_second_id(Link *link)
{
    if (!link)
    {
        return NO_ID;
    }
    return link->secondId;
}

Id link_get_pair_id(Link *link, Id id)
{
    if (!link || id == NO_ID)
    {
        return NO_ID;
    }
    if (link_get_first_id(link) == id)
        return link_get_second_id(link);
    if (link_get_second_id(link) == id)
        return link_get_first_id(link);

    return NO_ID;
}

int link_get_status(Link *link)
{
    if (!link)
    {
        return -1;
    }
    return link->status;
}

const char *link_get_name(Link *link)
{
    if (!link)
    {
        return NULL;
    }
    return link->name;
}

STATUS link_print(Link *link)
{
    if (!link)
    {
        return ERROR;
    }

    fprintf(stdout, "--> Link (Id: %ld; Name: %s; First linked Id: %ld, Second linked Id: %ld, Link status: %d)\n", link->id, link->name, link->firstId, link->secondId, link->status);
    return OK;
}
