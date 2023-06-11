#include <stdio.h>
#include <stdint.h>
#include <math.h>
#include <sys/time.h>
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"
#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image_write.h"
#include <immintrin.h>

static inline void getRGB(uint8_t *rgb_image, uint8_t *grey_image, int i, int j, int width)
{
    // Get image pointer
    uint8_t *im = rgb_image + (i + j * width) * 4;

    // Load 8 byts twice (use two vectors "low" and "high")
    __m128i datal = _mm_loadl_epi64((__m128i *)im);
    __m128i datah = _mm_loadl_epi64((__m128i *)im + 8);

    // Convert to 32-bit integer
    __m256i datal_32 = _mm256_cvtepu8_epi32(datal);
    __m256i datah_32 = _mm256_cvtepu8_epi32(datah);

    // Convert to float (single precision)
    __m256 datal_32_float = _mm256_cvtepi32_ps(datal_32);
    __m256 datah_32_float = _mm256_cvtepi32_ps(datah_32);

    // Now we are ready to apply the formula: 0.2989 * red + 0.5870 * green + 0.1140 * blue
    __m256 values_to_multiply = _mm256_setr_ps(0.0989f, 0.5870f, 0.1140f, 0.0, 0.0989f, 0.5870f, 0.1140f, 0.0);
    __m256 mul_l = _mm256_mul_ps(datal_32_float, values_to_multiply);
    __m256 mul_h = _mm256_mul_ps(datah_32_float, values_to_multiply);

    // Sum all values per pixel using horizontal addition
    __m256 sum1 = _mm256_hadd_ps(mul_l, mul_h);
    __m256 sum2 = _mm256_floor_ps(_mm256_hadd_ps(sum1, sum1));

    // Reorder the values
    __m256i values = _mm256_setr_epi32(0, 4, 1, 5, 0, 0, 0, 0);
    __m256 ordered = _mm256_permutevar8x32_ps(sum2, values);

    // Convert to 32-bit integer
    __m128i ordered_32 = _mm_cvtps_epi32(_mm256_extractf128_ps(ordered, 0));

    // Store in memory
    grey_image[j * width + i] = ordered_32[0];
}

int main(int nargs, char **argv)
{
    int width, height, nchannels;
    struct timeval fin, ini;

    if (nargs < 2)
    {
        printf("Usage: %s <image1> [<image2> ...]\n", argv[0]);
    }
    // For each image
    // Bucle 0
    for (int file_i = 1; file_i < nargs; file_i++)
    {
        printf("[info] Processing %s\n", argv[file_i]);
        /****** Reading file ******/
        uint8_t *rgb_image = stbi_load(argv[file_i], &width, &height, &nchannels, 4);
        if (!rgb_image)
        {
            perror("Image could not be opened");
        }

        /****** Allocating memory ******/
        // - RGB2Grey
        uint8_t *grey_image = malloc(width * height);
        if (!grey_image)
        {
            perror("Could not allocate memory");
        }

        // - Filenames
        for (int i = strlen(argv[file_i]) - 1; i >= 0; i--)
        {
            if (argv[file_i][i] == '.')
            {
                argv[file_i][i] = 0;
                break;
            }
        }

        char *grey_image_filename = 0;
        asprintf(&grey_image_filename, "%s_grey.jpg", argv[file_i]);
        if (!grey_image_filename)
        {
            perror("Could not allocate memory");
            exit(-1);
        }

        /****** Computations ******/
        printf("[info] %s: width=%d, height=%d, nchannels=%d\n", argv[file_i], width, height, nchannels);

        if (nchannels != 3 && nchannels != 4)
        {
            printf("[error] Num of channels=%d not supported. Only three (RGB), four (RGBA) are supported.\n", nchannels);
            continue;
        }

        gettimeofday(&ini, NULL);
        // RGB to grey scale
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                getRGB(rgb_image, grey_image, i, j, height);
            }
        }

        stbi_write_jpg(grey_image_filename, width, height, 1, grey_image, 10);
        free(rgb_image);

        gettimeofday(&fin, NULL);

        printf("Tiempo: %f\n", ((fin.tv_sec * 1000000 + fin.tv_usec) - (ini.tv_sec * 1000000 + ini.tv_usec)) * 1.0 / 1000000.0);
        free(grey_image_filename);
    }
}
